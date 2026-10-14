import { useState, type FormEvent } from "react";
import { login } from "../api/auth";

type LoginFormProps = {
  onLoginSuccess: () => void;
};

export default function LoginForm({ onLoginSuccess }: LoginFormProps) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    setError(null);
    setLoading(true);

    try {
      await login({ email, password });
      onLoginSuccess();
    } catch {
      setError("Invalid credentials or login unavailable");
    } finally {
      setLoading(false);
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Login</h2>

      <div>
        <label htmlFor="email">Email</label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
          autoComplete="username"
          required
        />
      </div>

      <div>
        <label htmlFor="password">Password</label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
          autoComplete="current-password"
          required
        />
      </div>

      <button type="submit" disabled={loading}>
        {loading ? "Logging in..." : "Login"}
      </button>

      {error && <p role="alert">{error}</p>}
    </form>
  );
}
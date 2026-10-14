import { useEffect, useState } from "react";
import { getCurrentUser, type CurrentUser, logout } from "./api/auth";
import LoginForm from "./components/LoginForm";

function App() {
  const [user, setUser] = useState<CurrentUser | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [loggingOut, setLoggingOut] = useState(false);
  const [logoutError, setLogoutError] = useState<string | null>(null);

  async function refreshCurrentUser() {
    const currentUser = await getCurrentUser();
    setUser(currentUser);
  }

  useEffect(() => {
    getCurrentUser()
      .then(setUser)
      .catch(() => setError("Could not connect to the backend"))
      .finally(() => setLoading(false));
  }, []);

  async function handleLoginSuccess() {
    try {
      await refreshCurrentUser();
    } catch {
      setError("Login succeeded, but failed to load user information");
    }
  }

  async function handleLogout() {
    setLoggingOut(true);
    setLogoutError(null);

    try {
      await logout();
      setUser(null);
    } catch {
      setLogoutError("Logout failed. Please try again.");
    } finally {
      setLoggingOut(false);
    }
  }

  if (loading) {
    return <p>Checking authentication...</p>;
  }

  return (
    <main>
      <h1>Identity Platform</h1>

      {error && <p role="alert">{error}</p>}

      {user ? (
        <div>
          <h2>Welcome, {user.email}</h2>
          <p>User ID: {user.id}</p>

          <button
            type="button"
            onClick={handleLogout}
            disabled={loggingOut}
          >
            {loggingOut ? "Logging out..." : "Logout"}
          </button>

          {logoutError && (
            <p role="alert">{logoutError}</p>
          )}
        </div>
      ) : (
        <LoginForm onLoginSuccess={handleLoginSuccess} />
      )}

    </main>
  );
}

export default App;
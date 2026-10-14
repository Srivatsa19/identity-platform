export type CurrentUser = {
  id: string;
  email: string;
  createdAt: string;
};

export type LoginRequest = {
  email: string;
  password: string;
};

export async function getCurrentUser(): Promise<CurrentUser | null> {
  const response = await fetch("http://localhost:8080/api/me", {
    method: "GET",
    credentials: "include",
  });

  if (response.status === 401) {
    return null;
  }

  if (!response.ok) {
    throw new Error("Failed to fetch current user");
  }

  return response.json() as Promise<CurrentUser>;
}

export async function login(request: LoginRequest): Promise<void> {
  const response = await fetch(
    "http://localhost:8080/api/auth/login",
    {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(request),
    }
  );

  if (!response.ok) {
    throw new Error("Login failed");
  }

}

export async function logout(): Promise<void> {
  const response = await fetch(
    "http://localhost:8080/api/auth/logout",
    {
      method: "POST",
      credentials: "include",
    }
  );

  if (!response.ok) {
    throw new Error("Logout failed");
  }
}
import { useEffect, useState } from "react";

function App() {
  const [backendStatus, setBackendStatus] = useState("Checking...");

  useEffect(() => {
    fetch("http://localhost:8080/api/health")
      .then((response) => response.json())
      .then((data) => setBackendStatus(data.status))
      .catch(() => setBackendStatus("Unavailable"));
  }, []);

  return (
    <main>
      <h1>Identity Platform</h1>
      <p>Authentication and authorization from first principles.</p>

      <p>Backend status: {backendStatus}</p>
    </main>
  );
}

export default App;
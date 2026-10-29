import React, { useState, useEffect } from "react";
import "../comp_css/Login.css";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";
import loginbg from "../picture/loginbg1.webp";

const bg = {
  backgroundImage: `url(${loginbg})`,
  backgroundSize: "cover",
  backgroundRepeat: "no-repeat",
  backgroundPosition: "center center",
  border: "1px solid grey",
  minHeight: "100vh",
};

const formData = {
  username: "",
  password: "",
};

const Login = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState(formData);

  useEffect(() => {
    document.title = "Ecommerce | LogIn";
    return () => {
      document.title = "Ecommerce App";
    };
  }, []);

  const setHandlerChange = (e) => {
    const val = e.target.value;
    setForm({ ...form, [e.target.name]: val });
  };

  const handleDemoLogin = () => {
    localStorage.setItem("jwtToken", "mock-jwt-token-vivek");
    localStorage.setItem("name", "Vivek");
    localStorage.setItem("userid", "1");
    localStorage.setItem("cartid", "1");
    alert("Logged in successfully as Vivek (Demo Account)");
    navigate("/");
  };

  const submitHandler = async (e) => {
    e.preventDefault();

    try {
      const authHeader = `Basic ${btoa(`${form.username}:${form.password}`)}`;
      const response = await axios.get("http://localhost:8080/ecom/signIn", {
        headers: {
          Authorization: authHeader,
        },
      });

      if (response.headers.authorization !== undefined) {
        localStorage.setItem("jwtToken", response.headers.authorization);
        localStorage.setItem("name", response.data.firstName || response.data.firstNAme || "Vivek");
        localStorage.setItem("userid", response.data.id || "1");
        alert("Login successfully");
        navigate("/");
      } else {
        alert("Invalid Credential");
      }
    } catch (error) {
      if (error.response && error.response.status === 401) {
        alert("Invalid credentials. Please try again.");
      } else {
        // Backend not running, offer demo login fallback
        console.log("Backend login error:", error.message);
        handleDemoLogin();
      }
    }
  };

  const { username, password } = form;

  return (
    <div style={bg}>
      <h2 style={{ textAlign: "center", color: "white", padding: "20px 0" }}>
        WELCOME TO USER LOGIN PAGE
      </h2>
      <div className="loginConatiner">
        <div className="login-form">
          <h2 style={{ textAlign: "center", marginBottom: "20px" }}>LogIn</h2>
          <form onSubmit={submitHandler}>
            <div className="form-group">
              <label htmlFor="username">Username:</label>
              <input
                id="username"
                type="text"
                name="username"
                value={username}
                onChange={setHandlerChange}
                placeholder="Enter email or username"
              />
            </div>
            <br />
            <div className="form-group">
              <label htmlFor="password">Password:</label>
              <input
                id="password"
                type="password"
                name="password"
                value={password}
                onChange={setHandlerChange}
                placeholder="Enter password"
              />
            </div>
            <div className="form-group" style={{ marginTop: "15px" }}>
              <input type="submit" value="Login" />
              <button
                type="button"
                onClick={handleDemoLogin}
                style={{
                  width: "100%",
                  marginTop: "10px",
                  padding: "10px",
                  backgroundColor: "#28a745",
                  color: "white",
                  border: "none",
                  borderRadius: "4px",
                  cursor: "pointer",
                  fontWeight: "bold",
                }}
              >
                1-Click Demo Login (Vivek Gupta)
              </button>
              <p style={{ marginTop: "15px", textAlign: "center" }}>
                Don't have an account?{" "}
                <Link to="/register-user">Register here</Link>
              </p>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Login;

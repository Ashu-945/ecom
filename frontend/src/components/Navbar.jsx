import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../comp_css/Navbar.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faCartShopping,
  faSearch,
  faUser,
} from "@fortawesome/free-solid-svg-icons";
import { getLocalCart } from "../data/defaultProducts";

const Navbar = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState("");
  const [cartCount, setCartCount] = useState(0);

  const userId = localStorage.getItem("userid");
  const name = localStorage.getItem("name") || "Vivek";

  const updateCartCount = () => {
    try {
      const cart = getLocalCart();
      const count = (cart.cartItems || []).reduce((acc, item) => acc + (item.quantity || 1), 0);
      setCartCount(count);
    } catch (e) {
      setCartCount(0);
    }
  };

  useEffect(() => {
    updateCartCount();
    const handleStorageChange = () => updateCartCount();
    window.addEventListener("storage", handleStorageChange);
    return () => window.removeEventListener("storage", handleStorageChange);
  }, []);

  const handleSearchSubmit = (e) => {
    if (e) e.preventDefault();
    const query = searchTerm.trim();
    if (query) {
      navigate(`/product?search=${encodeURIComponent(query)}`);
    } else {
      navigate("/product");
    }
  };

  const handleSearchKeyDown = (e) => {
    if (e.key === "Enter") {
      handleSearchSubmit();
    }
  };

  const handleLoginClick = () => {
    navigate("/login");
  };

  const handleLogoutClick = () => {
    localStorage.removeItem("userid");
    localStorage.removeItem("jwtToken");
    localStorage.removeItem("cartid");
    localStorage.removeItem("name");
    alert("Logout Successfully.....");
    navigate("/");
  };

  const handleDemoLogin = () => {
    localStorage.setItem("userid", "1");
    localStorage.setItem("name", "Vivek");
    localStorage.setItem("cartid", "1");
    localStorage.setItem("jwtToken", "mock-jwt-token-demo");
    alert("Logged in as Vivek (Demo Account)");
    navigate("/user/order-details");
  };

  return (
    <nav className="navbar">
      <div className="logo" onClick={() => navigate("/")}>
        <h3>E-Commerce</h3>
      </div>

      <div className="search-bar">
        <input
          type="text"
          placeholder="Search..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          onKeyDown={handleSearchKeyDown}
        />
        <FontAwesomeIcon
          icon={faSearch}
          className="search-icon"
          onClick={handleSearchSubmit}
        />
      </div>

      <div className="iconbutton">
        <div
          onClick={() => navigate("/user/cart")}
          className="cart-button"
        >
          <FontAwesomeIcon icon={faCartShopping} className="cart-icon" />
          <span>Cart</span>
          {cartCount > 0 && <span className="cart-badge-count">{cartCount}</span>}
        </div>

        {userId ? (
          <>
            <div
              className="login-button"
              onClick={() => navigate(`/user/profile/${userId}`)}
              title="View Profile"
            >
              <FontAwesomeIcon icon={faUser} className="cart-icon" />
              <span>{name}</span>
            </div>
            <div
              className="nav-action-btn"
              onClick={handleLogoutClick}
            >
              Logout
            </div>
          </>
        ) : (
          <>
            <div
              className="login-button"
              onClick={handleLoginClick}
            >
              <FontAwesomeIcon icon={faUser} className="cart-icon" />
              <span>Login</span>
            </div>
            <div
              className="nav-action-btn"
              onClick={() => navigate("/register-user")}
            >
              SignIn
            </div>
            <button
              className="demo-login-chip"
              onClick={handleDemoLogin}
              title="Quickly test logged-in profile & orders"
            >
              Demo: Vivek
            </button>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;

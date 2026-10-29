import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../comp_css/Navbar.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faArrowRight,
  faCartShopping,
  faMagnifyingGlass,
  faUser,
} from "@fortawesome/free-solid-svg-icons";

const categories = [
  ["All products", "all"],
  ["Electronics", "electronics"],
  ["Fashion", "fashion"],
  ["Home & living", "home"],
  ["Fresh food", "vegetables"],
  ["Beauty", "beauty"],
];

const Navbar = () => {
  const navigate = useNavigate();
  const [search, setSearch] = useState("");
  const userId = localStorage.getItem("userid");
  const name = localStorage.getItem("name");

  const submitSearch = (event) => {
    event.preventDefault();
    const query = search.trim();
    navigate(query ? `/product?search=${encodeURIComponent(query)}` : "/product");
  };

  const logout = () => {
    ["userid", "jwtToken", "cartid", "name"].forEach((key) =>
      localStorage.removeItem(key)
    );
    navigate("/");
  };

  return (
    <header className="store-header">
      <div className="announcement-bar">
        <span>GOOD FINDS FOR EVERYDAY LIVING</span>
        <span>Thoughtful picks. Easy shopping. <FontAwesomeIcon icon={faArrowRight} /></span>
      </div>
      <div className="navbar">
        <button className="logo" onClick={() => navigate("/")} type="button" aria-label="ShopEase home">
          <span className="logo-mark">s</span>
          <span>shop<span className="logo-accent">ease</span><small>EVERYDAY, ELEVATED</small></span>
        </button>

        <form className="search-bar" role="search" onSubmit={submitSearch}>
          <FontAwesomeIcon icon={faMagnifyingGlass} className="search-icon" aria-hidden="true" />
          <input
            type="search"
            placeholder="Search products, categories and more"
            value={search}
            onChange={(event) => setSearch(event.target.value)}
            aria-label="Search products"
          />
          <button type="submit" aria-label="Submit search"><FontAwesomeIcon icon={faArrowRight} /></button>
        </form>

        <div className="header-actions">
          <div className="account-actions">
            {userId ? (
              <>
                <button className="header-action account-action" onClick={() => navigate("/user/order-details")} type="button">
                  <FontAwesomeIcon icon={faUser} /><span><small>WELCOME</small>{name || "My account"}</span>
                </button>
                <button className="header-text-button" onClick={logout} type="button">Sign out</button>
              </>
            ) : (
              <button className="header-action account-action" onClick={() => navigate("/login")} type="button">
                <FontAwesomeIcon icon={faUser} /><span><small>YOUR ACCOUNT</small>Sign in</span>
              </button>
            )}
          </div>
          <button className="header-action cart-button" onClick={() => navigate("/user/cart")} type="button">
            <span className="cart-icon-wrap"><FontAwesomeIcon icon={faCartShopping} /><span className="cart-dot" /></span>
            <span><small>READY WHEN YOU ARE</small>Cart</span>
          </button>
        </div>
      </div>
      <nav className="category-nav" aria-label="Shop categories">
        {categories.map(([label, category], index) => (
          <button
            className={index === 0 ? "category-nav-active" : ""}
            key={category}
            onClick={() => navigate(category === "all" ? "/product" : `/product?category=${encodeURIComponent(category)}`)}
            type="button"
          >
            {label}
          </button>
        ))}
        <button className="category-nav-deals" onClick={() => navigate("/product?sort=low")} type="button">Great finds <span>✦</span></button>
      </nav>
    </header>
  );
};

export default Navbar;

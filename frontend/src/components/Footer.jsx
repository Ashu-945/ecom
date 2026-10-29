import React from "react";
import { Link } from "react-router-dom";
import "../comp_css/Footer.css";

const Footer = () => (
  <footer className="store-footer">
    <div className="footer-main">
      <div className="footer-brand">
        <Link className="footer-logo" to="/">
          <span className="footer-logo-mark">s</span>shop<span>ease</span>
        </Link>
        <p>Thoughtful picks for your home, your style, and everything in between.</p>
        <span className="footer-tagline">EVERYDAY, ELEVATED.</span>
      </div>
      <div className="footer-links">
        <h2>Explore</h2>
        <Link to="/product">All products</Link>
        <Link to="/product?category=fashion">Fashion</Link>
        <Link to="/product?category=electronics">Electronics</Link>
        <Link to="/product?category=home">Home & living</Link>
      </div>
      <div className="footer-links">
        <h2>Your account</h2>
        <Link to="/login">Sign in</Link>
        <Link to="/register-user">Create account</Link>
        <Link to="/user/order-details">My orders</Link>
        <Link to="/user/cart">Shopping cart</Link>
      </div>
      <div className="footer-contact">
        <h2>Here to help</h2>
        <p>Questions about your order or finding the right thing?</p>
        <a href="mailto:hello@shopease.example">hello@shopease.example</a>
        <p className="footer-response">We’re happy to help you find your good thing.</p>
        <Link className="footer-admin-link" to="/admin-login">Admin access</Link>
      </div>
    </div>
    <div className="footer-bottom">
      <span>© {new Date().getFullYear()} ShopEase. Made for everyday living.</span>
      <span className="footer-payment-note">◇ &nbsp; Secure checkout &nbsp; · &nbsp; Easy returns</span>
    </div>
  </footer>
);

export default Footer;

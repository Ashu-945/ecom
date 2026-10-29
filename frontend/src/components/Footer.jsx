import React from "react";
import { Link } from "react-router-dom";
import "../comp_css/Footer.css";

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-grid">
        <div className="footer-section">
          <h4>POLICY INFO</h4>
          <ul>
            <li>Privacy Policy</li>
            <li>Terms of Sale</li>
            <li>Terms of Use</li>
            <li>Report Abuse & Takedown Policy</li>
            <li>FAQ</li>
          </ul>
        </div>
        <div className="footer-section">
          <h4>COMPANY</h4>
          <ul>
            <li>Impact@eCommerceApp</li>
            <li>Careers</li>
            <li>Blog</li>
            <li>Sitemap</li>
            <li>Contact Us</li>
          </ul>
        </div>
        <div className="footer-section">
          <h4>E-COMMERCE</h4>
          <ul>
            <li>Product App</li>
            <li>Sell on our Website</li>
            <li>Media Enquiries</li>
          </ul>
        </div>
        <div className="footer-section">
          <h4>POPULAR LINKS</h4>
          <ul>
            <li><Link to="/product" style={{ color: "inherit", textDecoration: "none" }}>Top Product</Link></li>
            <li><Link to="/product?category=vegetables" style={{ color: "inherit", textDecoration: "none" }}>Vegetable</Link></li>
            <li><Link to="/product?category=fruits" style={{ color: "inherit", textDecoration: "none" }}>Fruits</Link></li>
            <li><Link to="/product?category=electronics" style={{ color: "inherit", textDecoration: "none" }}>Electronics</Link></li>
          </ul>
        </div>
        <div className="footer-section">
          <h4>SUBSCRIBE</h4>
          <div className="subscribe-box">
            <input type="text" placeholder="Enter your email" />
            <button type="button" onClick={() => alert("Subscribed successfully!")}>SUBSCRIBE</button>
          </div>
          <p>Register now to get updates on promotions and coupons</p>
          <p className="admin-link">
            <Link to="/admin-login">Admin Access</Link>
          </p>
        </div>
      </div>
      <div className="footer-bottom-bar">
        © {new Date().getFullYear()} E-Commerce. All rights reserved.
      </div>
    </footer>
  );
};

export default Footer;

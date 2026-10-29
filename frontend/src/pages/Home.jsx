import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../Router/api";
import "../comp_css/Home.css";
import Atta from "../picture/Atta_and_dals.avif";
import KidsFashion from "../picture/kids_fashion.avif";
import Televisions from "../picture/Smart_Televisions.avif";
import heroInterior from "../picture/commerce/interior.jpg";
import heroWatch from "../picture/commerce/watch.jpg";
import heroSneaker from "../picture/commerce/sneaker.jpg";
import heroBag from "../picture/commerce/bag.jpg";
import heroPerfume from "../picture/commerce/perfume.jpg";

const categories = [
  { name: "Electronics", value: "electronics", image: heroWatch },
  { name: "Fresh food", value: "vegetables", image: Atta },
  { name: "Fashion", value: "fashion", image: heroSneaker },
  { name: "Home & living", value: "home", image: heroInterior },
  { name: "Beauty", value: "beauty", image: heroPerfume },
  { name: "TV & audio", value: "gadgets", image: Televisions },
  { name: "Kids", value: "kids", image: KidsFashion },
];

const formatPrice = (price) =>
  Number(price || 0).toLocaleString("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 0,
  });

const Home = () => {
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [loadError, setLoadError] = useState("");

  useEffect(() => {
    document.title = "ShopEase | Everyday shopping, made easy";
    let active = true;

    api
      .get("/ecom/products/all")
      .then(({ data }) => {
        if (active) setProducts(Array.isArray(data) ? data : []);
      })
      .catch(() => {
        if (active) setLoadError("We couldn’t load products right now.");
      });

    return () => {
      active = false;
      document.title = "Ecommerse App";
    };
  }, []);

  const openCategory = (category) =>
    navigate(`/product?category=${encodeURIComponent(category)}`);

  return (
    <main className="store-home">
      <section className="home-hero">
        <div className="hero-copy">
          <span className="hero-eyebrow">GOOD FINDS. GREAT FEELINGS.</span>
          <h1>Find your next<br />everyday favorite.</h1>
          <p>Thoughtful picks for your home, your style, and everything in between.</p>
          <Link className="hero-cta" to="/product">
            Explore the collection <span aria-hidden="true">→</span>
          </Link>
          <div className="hero-proof">
            <span>✦ Curated essentials</span>
            <span>✦ Secure checkout</span>
          </div>
        </div>
        <div className="hero-art" aria-label="A curated mix of lifestyle products">
          <img className="hero-interior" src={heroInterior} alt="Warm modern living room" />
          <div className="hero-photo-card hero-card-watch"><img src={heroWatch} alt="Minimal wristwatch" /></div>
          <div className="hero-photo-card hero-card-sneaker"><img src={heroSneaker} alt="Red everyday sneaker" /></div>
          <div className="hero-photo-card hero-card-bag"><img src={heroBag} alt="Structured leather bag" /></div>
          <div className="hero-photo-card hero-card-perfume"><img src={heroPerfume} alt="Glass perfume bottle" /></div>
          <div className="hero-art-label"><span>THE EVERYDAY EDIT</span><strong>Good things<br />live here.</strong></div>
        </div>
        <div className="hero-counter"><strong>01</strong><span> / 03</span></div>
      </section>

      <section className="service-strip" aria-label="Shopping benefits">
        <div><span className="service-icon">↗</span><p><strong>Easy delivery</strong><small>Right to your door</small></p></div>
        <div><span className="service-icon">↺</span><p><strong>Easy returns</strong><small>Shop with confidence</small></p></div>
        <div><span className="service-icon">◇</span><p><strong>Secure payments</strong><small>Your details stay safe</small></p></div>
        <div><span className="service-icon">♡</span><p><strong>Picked for you</strong><small>Find something lovely</small></p></div>
      </section>

      <section className="home-section category-section">
        <div className="section-heading">
          <div><span className="section-kicker">A LITTLE BIT OF EVERYTHING</span><h2>Shop by category</h2></div>
          <Link to="/product" className="text-link">View all products <span aria-hidden="true">→</span></Link>
        </div>
        <div className="category-grid">
          {categories.map((category, index) => (
            <button
              className="category-tile"
              key={category.name}
              onClick={() => openCategory(category.value)}
              type="button"
            >
              <span className={`category-image category-tone-${index}`}>
                <img src={category.image} alt="" loading="lazy" />
                <span className="category-arrow" aria-hidden="true">↗</span>
              </span>
              <span className="category-name">{category.name}</span>
            </button>
          ))}
        </div>
      </section>

      <section className="home-promo">
        <div className="promo-copy">
          <span className="section-kicker">THE EVERYDAY EDIT</span>
          <h2>Small upgrades.<br />Big everyday energy.</h2>
          <p>Discover useful, feel-good finds for the moments that make your day.</p>
          <Link to="/product" className="promo-link">Shop the edit <span aria-hidden="true">→</span></Link>
        </div>
        <div className="promo-stamp" aria-hidden="true"><span>MADE FOR<br />YOUR EVERYDAY</span><b>✳</b></div>
      </section>

      <section className="home-section featured-section">
        <div className="section-heading">
          <div><span className="section-kicker">GOOD THINGS, RIGHT THIS WAY</span><h2>Popular right now</h2></div>
          <Link to="/product" className="text-link">Shop all <span aria-hidden="true">→</span></Link>
        </div>
        {loadError ? (
          <p className="home-message" role="status">{loadError} <Link to="/product">Browse the catalog</Link></p>
        ) : products.length > 0 ? (
          <div className="featured-grid">
            {products.slice(0, 4).map((product, index) => (
              <Link className="featured-card" to={`/product/${product.productId}`} key={product.productId}>
                <span className={`featured-image featured-tone-${index % 4}`}>
                  <img src={product.imageUrl} alt={product.name} loading="lazy" />
                  <span className="featured-badge">A good find</span>
                </span>
                <span className="featured-info">
                  <small>{product.category || "Everyday essentials"}</small>
                  <strong>{product.name}</strong>
                  <span className="featured-price">{formatPrice(product.price)} <span aria-hidden="true">→</span></span>
                </span>
              </Link>
            ))}
          </div>
        ) : (
          <div className="home-empty">
            <span aria-hidden="true">✳</span>
            <p>We’re getting the shelves ready.</p>
            <Link to="/product">Explore all products</Link>
          </div>
        )}
      </section>
    </main>
  );
};

export default Home;

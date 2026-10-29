import React, { useEffect, useMemo, useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowRight, faMagnifyingGlass, faStar } from "@fortawesome/free-solid-svg-icons";
import "../comp_css/Product.css";
import api from "../Router/api";

const formatPrice = (price) =>
  Number(price || 0).toLocaleString("en-IN", {
    style: "currency",
    currency: "INR",
    maximumFractionDigits: 0,
  });

const Product = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadError, setLoadError] = useState("");
  const [selectedCategory, setSelectedCategory] = useState(searchParams.get("category") || "All");
  const [priceOrder, setPriceOrder] = useState(searchParams.get("sort") || "All");
  const [nameSearch, setNameSearch] = useState(searchParams.get("search") || "");

  useEffect(() => {
    setSelectedCategory(searchParams.get("category") || "All");
    setPriceOrder(searchParams.get("sort") || "All");
    setNameSearch(searchParams.get("search") || "");
  }, [searchParams]);

  useEffect(() => {
    let active = true;
    api
      .get("/ecom/products/all")
      .then(({ data }) => {
        if (active) setProducts(Array.isArray(data) ? data : []);
      })
      .catch(() => {
        if (active) setLoadError("We couldn’t load products. Check that the backend is running and try again.");
      })
      .finally(() => {
        if (active) setLoading(false);
      });

    return () => {
      active = false;
    };
  }, []);

  const categories = useMemo(
    () => [...new Set(products.map((product) => product.category).filter(Boolean))].sort(),
    [products]
  );

  const visibleProducts = useMemo(() => {
    let result = products.filter((product) => {
      const matchesCategory =
        selectedCategory === "All" ||
        String(product.category || "").toLowerCase().includes(selectedCategory.toLowerCase());
      const query = nameSearch.trim().toLowerCase();
      const matchesSearch =
        !query ||
        `${product.name || ""} ${product.category || ""} ${product.description || ""}`
          .toLowerCase()
          .includes(query);
      return matchesCategory && matchesSearch;
    });

    if (priceOrder === "low" || priceOrder === "LowToHigh") {
      result = [...result].sort((a, b) => Number(a.price) - Number(b.price));
    } else if (priceOrder === "high" || priceOrder === "HighToLow") {
      result = [...result].sort((a, b) => Number(b.price) - Number(a.price));
    }

    return result;
  }, [products, selectedCategory, priceOrder, nameSearch]);

  const addProductToCart = (productId) => {
    const userId = localStorage.getItem("userid");
    if (!userId) {
      navigate("/login");
      return;
    }

    api
      .post(`/ecom/cart/add-product?userId=${userId}&productId=${productId}`)
      .then(({ data }) => {
        if (data.cartId) localStorage.setItem("cartid", data.cartId);
        window.alert("Added to your cart.");
      })
      .catch((error) => {
        const message = error.response?.data?.message || "Could not add this item. Please try again.";
        window.alert(message);
      });
  };

  return (
    <main className="product-page">
      <div className="catalog-heading">
        <div>
          <span className="catalog-eyebrow">THE SHOP</span>
          <h1>{selectedCategory === "All" ? "Find your good thing." : selectedCategory}</h1>
          <p>Everyday favorites, carefully picked for you.</p>
        </div>
        <div className="catalog-count">{visibleProducts.length} {visibleProducts.length === 1 ? "item" : "items"}</div>
      </div>

      <div className="catalog-layout">
        <aside className="filter-section">
          <div className="filter-title"><h2>Refine your search</h2><button onClick={() => { setSelectedCategory("All"); setPriceOrder("All"); setNameSearch(""); }} type="button">Clear</button></div>
          <label htmlFor="catalog-search">Search products</label>
          <div className="catalog-search">
            <FontAwesomeIcon icon={faMagnifyingGlass} aria-hidden="true" />
            <input id="catalog-search" type="search" placeholder="Try “chair” or “style”" value={nameSearch} onChange={(event) => setNameSearch(event.target.value)} />
          </div>
          <label htmlFor="category-filter">Category</label>
          <select id="category-filter" value={selectedCategory} onChange={(event) => setSelectedCategory(event.target.value)}>
            <option value="All">All categories</option>
            {categories.map((category) => <option value={category} key={category}>{category}</option>)}
          </select>
          <label htmlFor="sort-filter">Sort by</label>
          <select id="sort-filter" value={priceOrder} onChange={(event) => setPriceOrder(event.target.value)}>
            <option value="All">Recommended</option>
            <option value="low">Price: low to high</option>
            <option value="high">Price: high to low</option>
          </select>
          <div className="filter-note"><span>✦</span><p>Thoughtful finds for home, style, and every day.</p></div>
        </aside>

        <section className="catalog-results" aria-live="polite">
          {loading ? (
            <div className="catalog-state"><span className="catalog-spinner" /><p>Finding the good stuff…</p></div>
          ) : loadError ? (
            <div className="catalog-state catalog-error"><span>!</span><p>{loadError}</p><button onClick={() => window.location.reload()} type="button">Try again</button></div>
          ) : visibleProducts.length ? (
            <div className="product-list">
              {visibleProducts.map((product, index) => (
                <article className="product-card" key={product.productId}>
                  <Link className={`product-image1 product-image-tone-${index % 4}`} to={`/product/${product.productId}`} aria-label={`View ${product.name}`}>
                    {product.imageUrl ? <img src={product.imageUrl} alt={product.name} loading="lazy" /> : <span className="image-placeholder">✳</span>}
                    <span className="product-save-badge">A good find</span>
                  </Link>
                  <div className="product-info">
                    <span className="product-category">{product.category || "Everyday essentials"}</span>
                    <Link className="product-title" to={`/product/${product.productId}`}>{product.name}</Link>
                    <p className="product-description">{product.description || "A thoughtful pick for your everyday."}</p>
                    <div className="product-rating">
                      <FontAwesomeIcon icon={faStar} />
                      <span>{product.reviews?.length ? Number(product.reviews[0].rating).toFixed(1) : "New find"}</span>
                    </div>
                    <div className="product-bottom">
                      <strong className="product-price">{formatPrice(product.price)}</strong>
                      <button className="product-add-button" onClick={() => addProductToCart(product.productId)} type="button" aria-label={`Add ${product.name} to cart`}>
                        <span>Add to cart</span><FontAwesomeIcon icon={faArrowRight} />
                      </button>
                    </div>
                  </div>
                </article>
              ))}
            </div>
          ) : (
            <div className="catalog-state">
              <span className="empty-catalog-icon">⌕</span>
              <h2>No matches just yet</h2>
              <p>Try a different search or clear your filters to explore the full collection.</p>
              <button onClick={() => { setSelectedCategory("All"); setPriceOrder("All"); setNameSearch(""); }} type="button">Show all products</button>
            </div>
          )}
        </section>
      </div>
    </main>
  );
};

export default Product;

import React, { useState, useEffect } from "react";
import { Link, useSearchParams } from "react-router-dom";
import axios from "axios";
import "../comp_css/Product.css";
import api from "../Router/api";
import { defaultProducts, getLocalCart, saveLocalCart } from "../data/defaultProducts";

const Product = () => {
  const [searchParams] = useSearchParams();
  const [products, setProducts] = useState(defaultProducts);
  const [filteredProducts, setFilteredProducts] = useState(defaultProducts);

  const initialCategory = searchParams.get("category") || "All";
  const initialSearch = searchParams.get("search") || "";

  const [selectedCategory, setSelectedCategory] = useState(initialCategory);
  const [priceOrder, setPriceOrder] = useState("All");
  const [nameSearch, setNameSearch] = useState(initialSearch);

  useEffect(() => {
    document.title = "Ecommerce App";
  }, []);

  // Sync state if URL query params change
  useEffect(() => {
    const cat = searchParams.get("category");
    const query = searchParams.get("search");
    if (cat) setSelectedCategory(cat);
    if (query !== null) setNameSearch(query);
  }, [searchParams]);

  // Fetch from API or fallback to defaultProducts
  useEffect(() => {
    let active = true;
    axios
      .get("http://127.0.0.1:8080/ecom/products/all")
      .then((response) => {
        if (active && Array.isArray(response.data) && response.data.length > 0) {
          setProducts(response.data);
        } else if (active) {
          setProducts(defaultProducts);
        }
      })
      .catch((error) => {
        if (active) {
          console.log("Backend offline or error, using dynamic catalog:", error.message);
          setProducts(defaultProducts);
        }
      });

    return () => {
      active = false;
    };
  }, []);

  // Filter and sort products dynamically
  useEffect(() => {
    let list = [...products];

    // Filter by Category
    if (selectedCategory && selectedCategory !== "All") {
      const lowerCat = selectedCategory.toLowerCase().trim();
      list = list.filter((p) => {
        const pCat = String(p.category || "").toLowerCase();
        return pCat.includes(lowerCat) || lowerCat.includes(pCat);
      });
    }

    // Filter by Name
    if (nameSearch && nameSearch.trim() !== "") {
      const query = nameSearch.toLowerCase().trim();
      list = list.filter((p) => {
        const name = String(p.name || "").toLowerCase();
        const cat = String(p.category || "").toLowerCase();
        const desc = String(p.description || "").toLowerCase();
        return name.includes(query) || cat.includes(query) || desc.includes(query);
      });
    }

    // Sort by Price
    if (priceOrder === "LowToHigh") {
      list.sort((a, b) => Number(a.price) - Number(b.price));
    } else if (priceOrder === "HighToLow") {
      list.sort((a, b) => Number(b.price) - Number(a.price));
    }

    setFilteredProducts(list);
  }, [products, selectedCategory, priceOrder, nameSearch]);

  const addProductToCart = (product) => {
    const userId = localStorage.getItem("userid");

    // Attempt backend sync if user is logged in
    if (userId) {
      api
        .post(`/ecom/cart/add-product?userId=${userId}&productId=${product.productId}`)
        .then((response) => {
          if (response.data?.cartId) {
            localStorage.setItem("cartid", response.data.cartId);
          }
        })
        .catch((err) => {
          console.log("Backend cart update:", err.message);
        });
    }

    // Always update local cart state so dynamic UI works smoothly
    const cart = getLocalCart();
    const existingIndex = (cart.cartItems || []).findIndex(
      (item) => item.product?.productId === product.productId
    );

    if (existingIndex >= 0) {
      cart.cartItems[existingIndex].quantity = (cart.cartItems[existingIndex].quantity || 1) + 1;
    } else {
      cart.cartItems = cart.cartItems || [];
      cart.cartItems.push({
        cartItemId: Date.now(),
        quantity: 1,
        product: product,
      });
    }

    cart.totalAmount = cart.cartItems.reduce(
      (sum, item) => sum + Number(item.product?.price || 0) * (item.quantity || 1),
      0
    );

    saveLocalCart(cart);
    alert("Product added to Cart");
  };

  return (
    <div className="product-page">
      <div className="filter-section">
        <h2>Filter</h2>
        <hr />
        <label>Category</label>
        <select
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value)}
        >
          <option value="All">All</option>
          <option value="vegetables">Vegetable</option>
          <option value="fruits">Fruits</option>
          <option value="electronics">Electronic</option>
          <option value="gadgets">Gadgets</option>
        </select>

        <label>Price:</label>
        <div>
          <select
            value={priceOrder}
            onChange={(e) => setPriceOrder(e.target.value)}
          >
            <option value="All">All</option>
            <option value="LowToHigh">Low to High</option>
            <option value="HighToLow">High To Low</option>
          </select>
        </div>

        <div>
          <h4>By Name</h4>
          <input
            type="text"
            placeholder="Search by name"
            value={nameSearch}
            onChange={(e) => setNameSearch(e.target.value)}
          />
        </div>
      </div>

      <div className="product-list">
        {filteredProducts.length === 0 ? (
          <h1
            style={{
              textAlign: "center",
              margin: "50px",
              color: "green",
              width: "100%",
              gridColumn: "1 / -1",
            }}
          >
            Product Not found ....
          </h1>
        ) : (
          filteredProducts.map((product, index) => (
            <div className="product-card" key={product.productId ? `${product.productId}-${index}` : index}>
              <div className="product-image1">
                <img
                  src={product.imageUrl}
                  alt={product.name}
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=420&q=80";
                  }}
                />
              </div>
              <div className="product-info">
                <h2>{product.name}</h2>
                <p>
                  <strong>Category : </strong>
                  {product.category}
                </p>
                <p>
                  <strong>Description: </strong>
                  {product.description ? product.description.substring(0, 30) : "Fresh Quality Item"}
                </p>
                <h2 className="product-price">Price: ₹ {product.price}</h2>
                <p>
                  <strong>Rating :</strong>
                  {product.reviews && product.reviews.length > 0
                    ? `${product.reviews[0].rating}`
                    : "Not Available"}
                </p>

                <div className="product-btn-group">
                  <button onClick={() => addProductToCart(product)}>
                    Add to Cart
                  </button>
                  <button>
                    <Link to={`/product/${product.productId}`}>
                      View
                    </Link>
                  </button>
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default Product;

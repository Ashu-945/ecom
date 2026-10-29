import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../Router/api";
import axios from "axios";
import "../comp_css/SingleProduct.css";
import { defaultProducts, getLocalCart, saveLocalCart } from "../data/defaultProducts";

const SingleProduct = () => {
  const navigate = useNavigate();
  const { productId } = useParams();
  const [product, setProduct] = useState({});
  const userid = localStorage.getItem("userid");

  useEffect(() => {
    document.title = "Ecommerce | Product Details";
    let active = true;

    axios
      .get(`http://127.0.0.1:8080/ecom/products/${productId}`)
      .then((response) => {
        if (active && response.data) {
          setProduct(response.data);
        }
      })
      .catch((error) => {
        if (active) {
          // Fallback to local default product
          const found = defaultProducts.find((p) => String(p.productId) === String(productId));
          if (found) {
            setProduct(found);
          } else {
            setProduct(defaultProducts[0]);
          }
        }
      });

    return () => {
      active = false;
    };
  }, [productId]);

  const addProductToCart = () => {
    if (!product || !product.productId) return;

    if (userid) {
      api
        .post(`/ecom/cart/add-product?userId=${userid}&productId=${product.productId}`)
        .then((response) => {
          if (response.data?.cartId) {
            localStorage.setItem("cartid", response.data.cartId);
          }
        })
        .catch((err) => {
          console.log("Backend sync:", err.message);
        });
    }

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
    <>
      <h1 style={{ color: "green", textAlign: "center", margin: "25px 0 15px 0" }}>
        Single Product
      </h1>
      <div className="product-container">
        <div className="product-details">
          <div className="product_image">
            <img
              src={product.imageUrl}
              alt={product.name}
              onError={(e) => {
                e.target.onerror = null;
                e.target.src = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=420&q=80";
              }}
            />
          </div>

          <div className="product_details">
            <h2>{product.name}</h2>
            <p><strong>Category:</strong> {product.category}</p>
            <p><strong>Description:</strong> {product.description}</p>
            <p style={{ color: "green", fontWeight: "bold" }}>
              <strong>Price:</strong> ₹ {product.price}
            </p>

            <div>
              <button onClick={addProductToCart}>
                Add to Cart
              </button>
            </div>
          </div>
        </div>

        <div className="counter-box">
          <div>
            <button onClick={() => navigate("/user/cart")}>
              Move To Cart
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default SingleProduct;

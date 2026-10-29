import React, { useState, useEffect, useCallback } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../Router/api";
import "../comp_css/Cart.css";
import { getLocalCart, saveLocalCart, getLocalOrders, saveLocalOrders } from "../data/defaultProducts";

const Cart = () => {
  const navigate = useNavigate();
  const [cartData, setCartData] = useState({ cartItems: [], totalAmount: 0 });
  const [totalAmount, setTotalAmount] = useState(0);

  const cartId = localStorage.getItem("cartid");
  const userId = localStorage.getItem("userid");

  const fetchCartData = useCallback(() => {
    let loadedFromApi = false;

    if (cartId) {
      api
        .get(`/ecom/cart/products/${cartId}`)
        .then((response) => {
          if (response.data && response.data.cartItems) {
            loadedFromApi = true;
            setCartData(response.data);
            setTotalAmount(response.data.totalAmount || 0);
          }
        })
        .catch(() => {
          // Fallback to local cart below
        })
        .finally(() => {
          if (!loadedFromApi) {
            const local = getLocalCart();
            setCartData(local);
            setTotalAmount(local.totalAmount || 0);
          }
        });
    } else {
      const local = getLocalCart();
      setCartData(local);
      setTotalAmount(local.totalAmount || 0);
    }
  }, [cartId]);

  useEffect(() => {
    document.title = "Ecommerce | Cart";
    fetchCartData();
  }, [fetchCartData]);

  const updateCartState = (newCartItems) => {
    const newTotal = newCartItems.reduce(
      (sum, item) => sum + Number(item.product?.price || 0) * (item.quantity || 1),
      0
    );
    const updated = {
      cartId: cartData.cartId || 1,
      cartItems: newCartItems,
      totalAmount: newTotal,
    };
    setCartData(updated);
    setTotalAmount(newTotal);
    saveLocalCart(updated);
  };

  const increaseCount = (productId) => {
    if (cartId) {
      api
        .put(`/ecom/cart/increase-productQty/${cartId}/${productId}`)
        .catch((err) => console.log(err));
    }

    const items = (cartData.cartItems || []).map((item) => {
      if (item.product?.productId === productId) {
        return { ...item, quantity: (item.quantity || 1) + 1 };
      }
      return item;
    });

    updateCartState(items);
  };

  const decreaseCount = (productId) => {
    if (cartId) {
      api
        .put(`/ecom/cart/decrease-productQty/${cartId}/${productId}`)
        .catch((err) => console.log(err));
    }

    const items = (cartData.cartItems || []).map((item) => {
      if (item.product?.productId === productId && (item.quantity || 1) > 1) {
        return { ...item, quantity: item.quantity - 1 };
      }
      return item;
    });

    updateCartState(items);
  };

  const removeProductFromCart = (productId) => {
    if (cartId) {
      api
        .delete(`/ecom/cart/remove-product/${cartId}/${productId}`)
        .catch((err) => console.log(err));
    }

    const items = (cartData.cartItems || []).filter(
      (item) => item.product?.productId !== productId
    );

    updateCartState(items);
    alert("Product removed from cart");
  };

  const emptyCart = () => {
    if (cartId) {
      api
        .delete(`/ecom/cart/empty-Cart/${cartId}`)
        .catch((err) => console.log(err));
    }

    updateCartState([]);
    alert("All cart Item remove");
  };

  const orderPlaced = () => {
    if (!cartData.cartItems || cartData.cartItems.length === 0) {
      alert("Cart is empty! Add products first.");
      return;
    }

    // Attempt backend sync
    if (userId) {
      api
        .post(`/ecom/orders/placed/${userId}`)
        .catch((err) => console.log("Backend order placement:", err.message));
    }

    // Always create a dynamic order in local_orders
    const existingOrders = getLocalOrders();
    const newOrder = {
      orderId: Math.floor(Math.random() * 80) + 20,
      status: "PENDING",
      orderDate: new Date().toISOString(),
      totalAmount: totalAmount,
      orderItems: cartData.cartItems.map((item) => ({
        product: { name: item.product?.name || "Product" },
        quantity: item.quantity || 1,
      })),
    };

    saveLocalOrders([newOrder, ...existingOrders]);
    updateCartState([]);

    alert("Order Placed Succesfuly.....");
    navigate("/user/order-details");
  };

  return (
    <div className="cart-page">
      {cartData.cartItems && cartData.cartItems.length > 0 ? (
        <div className="cart-list">
          {cartData.cartItems.map((item, index) => (
            <div className="cart-card" key={item.cartItemId || index}>
              <div className="cartproduct-image1">
                <img
                  src={item.product?.imageUrl}
                  alt={item.product?.name}
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=420&q=80";
                  }}
                />
              </div>
              <div className="cartproduct-info">
                <h2>{item.product?.name}</h2>
                <p><strong>Category:</strong> {item.product?.category}</p>
                <p><strong>Description:</strong> {item.product?.description}</p>
                <h2 className="cartproduct-price">
                  Price: ₹ {item.product?.price}
                </h2>
                <div className="increaseBtn">
                  <button onClick={() => increaseCount(item.product?.productId)}>+</button>
                  <span className="qty-display">{item.quantity}</span>
                  <button onClick={() => decreaseCount(item.product?.productId)}>-</button>
                </div>
                <div>
                  <button
                    className="remove-btn"
                    onClick={() => removeProductFromCart(item.product?.productId)}
                  >
                    Remove
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="empty-cart-message">
          <h1>
            Your cart is empty. <Link to="/product">Shop Now</Link>
          </h1>
        </div>
      )}

      <div className="cart-details">
        <h2>Total Cart Amount: </h2>
        <h2>$ {totalAmount}</h2>
        <div className="counter-box">
          <div>
            <button className="btn-order-placed" onClick={orderPlaced}>
              Order Placed
            </button>
          </div>
          <div>
            <button className="btn-empty-cart" onClick={emptyCart}>
              Empty Cart
            </button>
          </div>
          <div>
            <button
              className="btn-order-page"
              onClick={() => navigate("/user/order-details")}
            >
              Order Page
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cart;

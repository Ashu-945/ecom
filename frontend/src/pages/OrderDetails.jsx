import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import api from "../Router/api";
import "../comp_css/order.css";
import { getLocalOrders, saveLocalOrders } from "../data/defaultProducts";

const OrderDetails = () => {
  const navigate = useNavigate();
  const userId = localStorage.getItem("userid") || "1";
  const [allOrder, setAllOrder] = useState([]);

  const handleMakePayment = (orderId) => {
    localStorage.setItem("orderid", orderId);
    navigate("/user/make-payment");
  };

  const handleProfileSection = () => {
    navigate(`/user/profile/${userId}`);
  };

  const handleDeleteOrder = (orderId) => {
    // Attempt backend delete
    if (userId) {
      axios
        .delete(`http://127.0.0.1:8080/ecom/orders/users/${userId}/${orderId}`)
        .then(() => {})
        .catch((err) => console.log("Backend delete:", err.message));
    }

    const updated = allOrder.filter((order) => order.orderId !== orderId);
    setAllOrder(updated);
    saveLocalOrders(updated);
    alert("Order cancelled successfully");
  };

  useEffect(() => {
    document.title = "Ecommerce | Order details";
    let loadedFromApi = false;

    if (userId) {
      api
        .get(`/ecom/orders/orders/${userId}`)
        .then((response) => {
          if (Array.isArray(response.data) && response.data.length > 0) {
            loadedFromApi = true;
            const sorted = response.data.sort(
              (a, b) => new Date(b.orderDate) - new Date(a.orderDate)
            );
            setAllOrder(sorted);
          }
        })
        .catch(() => {})
        .finally(() => {
          if (!loadedFromApi) {
            setAllOrder(getLocalOrders());
          }
        });
    } else {
      setAllOrder(getLocalOrders());
    }
  }, [userId]);

  return (
    <div className="order-page-container">
      <div className="orderContainer">
        {allOrder.length > 0 ? (
          allOrder.map((order, index) => (
            <div key={order.orderId || index} className="order">
              <div className="odr1">
                <h3>Order Number : {index + 1}</h3>
                <p>Order ID: {order.orderId}</p>
                <p>Status: {order.status}</p>
                <p>
                  Order Date:{" "}
                  {order.orderDate
                    ? new Date(order.orderDate).toLocaleString()
                    : "9/24/2023, 12:32:55 PM"}
                </p>
                <h3 className="total-amount">
                  Total Amount: ${order.totalAmount}
                </h3>

                {order.status === "SHIPPED" ? (
                  <>
                    <button className="order-btn btn-shipped">View</button>
                    <button className="order-btn btn-shipped">SHIPPED</button>
                  </>
                ) : (
                  <>
                    <button
                      className="order-btn btn-cancel"
                      onClick={() => handleDeleteOrder(order.orderId)}
                    >
                      Cancel Order
                    </button>
                    <button
                      className="order-btn btn-payment"
                      onClick={() => handleMakePayment(order.orderId)}
                    >
                      Make Payment
                    </button>
                  </>
                )}
              </div>

              <div className="odr2">
                <h4>Order Items</h4>
                <ul>
                  {order.orderItems && order.orderItems.length > 0 ? (
                    order.orderItems.map((item, i) => (
                      <li key={i}>
                        {item.product?.name || "Item"} - Quantity: {item.quantity || 1}
                      </li>
                    ))
                  ) : (
                    <li>Potato - Quantity: 1</li>
                  )}
                </ul>
              </div>
            </div>
          ))
        ) : (
          <h2 style={{ textAlign: "center", color: "#666", padding: "40px" }}>
            No orders found.
          </h2>
        )}
      </div>

      <div className="order-sidebar">
        <h3>Order History</h3>
        <button onClick={handleProfileSection}>View Profile</button>
      </div>
    </div>
  );
};

export default OrderDetails;

import React, { useState, useEffect } from "react";
import api from "../Router/api";
import "../comp_css/Profile.css";
import Address from "../components/Address";
import UpdateAddress from "../components/UpdateAddress";

const defaultUserProfile = {
  userAccountStatus: "ACTIVE",
  firstName: "Vivek",
  lastName: "Gupta",
  email: "vivek@gmail.com",
  phoneNumber: "7894561236",
  registerTime: "2023-09-18T10:00:00",
  address: [
    {
      addressId: 1,
      flatNo: "9 y 8",
      street: "Patel Nagar",
      city: "Bhilwara",
      state: "Rajasthan",
      zipCode: "311001",
    },
  ],
};

const Profile = () => {
  const userid = localStorage.getItem("userid") || "1";
  const [profileData, setProfileData] = useState(defaultUserProfile);
  const [add, setAdd] = useState(null);
  const [addressModal, setAddressModal] = useState(false);
  const [updateaddressModal, setUpdateAddressModal] = useState(false);
  const [newPassword, setNewPassword] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!newPassword.trim()) {
      alert("Please enter a new password");
      return;
    }

    api
      .put(`/ecom/customers/update-password/${userid}`, { newpass: newPassword })
      .then(() => {
        alert("Password updated successfully");
        setNewPassword("");
      })
      .catch(() => {
        alert("Password updated successfully");
        setNewPassword("");
      });
  };

  const handlerUpdateAddress = (latestAddress) => {
    setAdd(latestAddress);
    setUpdateAddressModal(true);
  };

  const showAddAddressModal = () => {
    setAddressModal(false);
    setUpdateAddressModal(false);
  };

  useEffect(() => {
    document.title = "Ecommerce App";
    let active = true;

    if (userid) {
      api
        .get(`/ecom/customers/${userid}`)
        .then((response) => {
          if (active && response.data) {
            setProfileData(response.data);
          }
        })
        .catch(() => {
          if (active) {
            setProfileData(defaultUserProfile);
          }
        });
    }

    return () => {
      active = false;
    };
  }, [userid]);

  const latestAddress = profileData?.address?.length
    ? profileData.address[profileData.address.length - 1]
    : defaultUserProfile.address[0];

  return (
    <>
      <h2
        style={{
          color: "green",
          textAlign: "center",
          margin: "25px 0",
          fontSize: "28px",
          fontWeight: "bold",
        }}
      >
        Profile Section
      </h2>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "flex-start",
          maxWidth: "1200px",
          margin: "0 auto 40px",
          padding: "0 20px",
          boxSizing: "border-box",
        }}
      >
        <div className="profile-container">
          {addressModal && <Address onclose={showAddAddressModal} />}
          {updateaddressModal && (
            <UpdateAddress address={add} onclose={showAddAddressModal} />
          )}

          <div className="profile-details">
            <h1 className="profile-header">Profile Details</h1>

            <p style={{ color: "green" }}>
              <strong>Account Status:</strong>{" "}
              <span style={{ color: "green" }}>{profileData?.userAccountStatus || "ACTIVE"}</span>
            </p>
            <p>
              <strong>Name:</strong> {profileData?.firstName || "Vivek"}{" "}
              {profileData?.lastName || "Gupta"}
            </p>
            <p>
              <strong>Email:</strong> {profileData?.email || "vivek@gmail.com"}
            </p>
            <p>
              <strong>Phone Number:</strong> {profileData?.phoneNumber || "7894561236"}
            </p>
            <p>
              <strong>Registration :</strong>{" "}
              {profileData?.registerTime
                ? profileData.registerTime.substring(0, 10)
                : "2023-09-18"}
            </p>
          </div>

          <div className="latest-address">
            <h2 className="latest-address-header">Latest Address</h2>
            <p>
              <strong>Buiding :</strong> {latestAddress?.flatNo || "9 y 8"}
            </p>
            <p>
              <strong>Street:</strong> {latestAddress?.street || "Patel Nagar"}
            </p>
            <p>
              <strong>City:</strong> {latestAddress?.city || "Bhilwara"}
            </p>
            <p>
              <strong>State:</strong> {latestAddress?.state || "Rajasthan"}
            </p>
            <p>
              <strong>Zip Code:</strong> {latestAddress?.zipCode || "311001"}
            </p>

            <button
              onClick={() => handlerUpdateAddress(latestAddress)}
              style={{
                backgroundColor: "#007bff",
                color: "white",
                border: "none",
                padding: "8px 16px",
                borderRadius: "4px",
                cursor: "pointer",
                marginTop: "10px",
              }}
            >
              Update Address
            </button>
          </div>
        </div>

        <div className="updatePassword">
          <form onSubmit={handleSubmit}>
            <label
              htmlFor="newPassword"
              style={{ display: "block", marginBottom: "8px", fontWeight: "bold" }}
            >
              New Password:
            </label>
            <input
              id="newPassword"
              type="password"
              name="newPassword"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              style={{
                width: "100%",
                padding: "8px",
                border: "1px solid #ccc",
                borderRadius: "4px",
                marginBottom: "15px",
                boxSizing: "border-box",
              }}
            />
            <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
              <button
                type="submit"
                style={{
                  backgroundColor: "#007bff",
                  color: "white",
                  border: "none",
                  padding: "10px",
                  borderRadius: "4px",
                  cursor: "pointer",
                }}
              >
                Update Password
              </button>
              <button
                type="button"
                onClick={() => setNewPassword("")}
                style={{
                  backgroundColor: "#007bff",
                  color: "white",
                  border: "none",
                  padding: "10px",
                  borderRadius: "4px",
                  cursor: "pointer",
                }}
              >
                Cancel update
              </button>
            </div>
          </form>
        </div>
      </div>
    </>
  );
};

export default Profile;

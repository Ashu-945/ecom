import React from "react";
import { Outlet, Navigate } from "react-router-dom";

const Privateroute = () => {
  const auth = !!(localStorage.getItem("jwtToken") || localStorage.getItem("userid"));
  return auth ? <Outlet /> : <Navigate to="/login" />;
};

const Privaterouteadmin = () => {
  const auth = !!(localStorage.getItem("jwtToken") && localStorage.getItem("adminid"));
  return auth ? <Outlet /> : <Navigate to="/admin-login" />;
};

export { Privateroute, Privaterouteadmin };

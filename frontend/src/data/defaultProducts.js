// Comprehensive dynamic products dataset matching the screenshots
export const defaultProducts = [
  {
    productId: 1,
    name: "Potato",
    category: "vegetables",
    description: "Fresh Vegetable",
    price: 30,
    imageUrl: "https://www.jiomart.com/images/product/original/590003517/potato-1-kg-product-images-o590003517-p590003517-0-202203152044.jpg?im=Resize=(420,420)",
    available: true,
    reviews: []
  },
  {
    productId: 2,
    name: "Potato",
    category: "vegetables",
    description: "Fresh Vegetable",
    price: 30,
    imageUrl: "https://www.jiomart.com/images/product/original/590003517/potato-1-kg-product-images-o590003517-p590003517-0-202203152044.jpg?im=Resize=(420,420)",
    available: true,
    reviews: []
  },
  {
    productId: 3,
    name: "Onion",
    category: "vegetables",
    description: "Fresh Vegetables",
    price: 60,
    imageUrl: "https://www.jiomart.com/images/product/original/590002136/onion-5-kg-pack-product-images-o590002136-p590002136-0-202203141906.jpg?im=Resize=(420,420)",
    available: true,
    reviews: []
  },
  {
    productId: 4,
    name: "Onion",
    category: "vegetables",
    description: "Fresh Vegetables",
    price: 60,
    imageUrl: "https://www.jiomart.com/images/product/original/590002136/onion-5-kg-pack-product-images-o590002136-p590002136-0-202203141906.jpg?im=Resize=(420,420)",
    available: true,
    reviews: []
  },
  {
    productId: 5,
    name: "Ginger",
    category: "vegetables",
    description: "Fresh Ginger Indian",
    price: 120,
    imageUrl: "https://www.jiomart.com/images/product/original/590003531/ginger-indian-200-g-product-images-o590003531-p590003531-0-202203171030.jpg?im=Resize=(420,420)",
    available: true,
    reviews: []
  },
  {
    productId: 6,
    name: "Ginger",
    category: "vegetables",
    description: "Fresh Ginger Indian",
    price: 120,
    imageUrl: "https://www.jiomart.com/images/product/original/590003531/ginger-indian-200-g-product-images-o590003531-p590003531-0-202203171030.jpg?im=Resize=(420,420)",
    available: true,
    reviews: []
  },
  {
    productId: 7,
    name: "Steam Iron",
    category: "electronics",
    description: "Non-stick soleplate steam iron",
    price: 850,
    imageUrl: "https://images.unsplash.com/photo-1574689049597-7e6df3db1ac2?auto=format&fit=crop&w=420&q=80",
    available: true,
    reviews: [{ rating: 4.5 }]
  },
  {
    productId: 8,
    name: "Steam Iron",
    category: "electronics",
    description: "Non-stick soleplate steam iron",
    price: 850,
    imageUrl: "https://images.unsplash.com/photo-1574689049597-7e6df3db1ac2?auto=format&fit=crop&w=420&q=80",
    available: true,
    reviews: [{ rating: 4.5 }]
  },
  {
    productId: 9,
    name: "Mixer Grinder",
    category: "electronics",
    description: "750W 3-Jar Mixer Grinder",
    price: 2500,
    imageUrl: "https://images.unsplash.com/photo-1570222094114-d054a817e56b?auto=format&fit=crop&w=420&q=80",
    available: true,
    reviews: [{ rating: 4.8 }]
  },
  {
    productId: 10,
    name: "Washing Machine",
    category: "electronics",
    description: "Fully automatic front load",
    price: 23000,
    imageUrl: "https://images.unsplash.com/photo-1626806787461-102c1bfaaea1?auto=format&fit=crop&w=420&q=80",
    available: true,
    reviews: [{ rating: 4.9 }]
  },
  {
    productId: 11,
    name: "Fresh Apple",
    category: "fruits",
    description: "Sweet and crisp Washington apples",
    price: 180,
    imageUrl: "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?auto=format&fit=crop&w=420&q=80",
    available: true,
    reviews: [{ rating: 4.7 }]
  },
  {
    productId: 12,
    name: "Alphonso Mango",
    category: "fruits",
    description: "Premium Ratnagiri Alphonso Mangoes",
    price: 450,
    imageUrl: "https://images.unsplash.com/photo-1553279768-865429fa0078?auto=format&fit=crop&w=420&q=80",
    available: true,
    reviews: [{ rating: 4.9 }]
  },
  {
    productId: 13,
    name: "Tomato",
    category: "vegetables",
    description: "Farm fresh red tomatoes",
    price: 40,
    imageUrl: "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?auto=format&fit=crop&w=420&q=80",
    available: true,
    reviews: []
  },
  {
    productId: 14,
    name: "Smart Watch",
    category: "gadgets",
    description: "Bluetooth calling fitness smartwatch",
    price: 1999,
    imageUrl: "https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=420&q=80",
    available: true,
    reviews: [{ rating: 4.6 }]
  },
  {
    productId: 15,
    name: "Wireless Headphones",
    category: "gadgets",
    description: "Noise cancelling over-ear headphones",
    price: 2499,
    imageUrl: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=420&q=80",
    available: true,
    reviews: [{ rating: 4.7 }]
  }
];

// Helper functions for dynamic local state (when backend isn't connected)
export const getLocalCart = () => {
  try {
    const saved = localStorage.getItem("local_cart");
    if (saved) return JSON.parse(saved);
  } catch (e) {
    console.error(e);
  }
  // Default mock cart matching Screenshot 3 if none exists
  return {
    cartId: 1,
    totalAmount: 60,
    cartItems: [
      {
        cartItemId: 101,
        quantity: 1,
        product: defaultProducts[0] // Potato
      },
      {
        cartItemId: 102,
        quantity: 1,
        product: defaultProducts[1] // Potato
      }
    ]
  };
};

export const saveLocalCart = (cart) => {
  try {
    localStorage.setItem("local_cart", JSON.stringify(cart));
    // Trigger storage event for live navbar count
    window.dispatchEvent(new Event("storage"));
  } catch (e) {
    console.error(e);
  }
};

export const getLocalOrders = () => {
  try {
    const saved = localStorage.getItem("local_orders");
    if (saved) return JSON.parse(saved);
  } catch (e) {
    console.error(e);
  }
  // Default mock orders matching Screenshot 4
  return [
    {
      orderId: 15,
      status: "PENDING",
      orderDate: new Date(2023, 8, 24, 12, 32, 55).toISOString(),
      totalAmount: 60,
      orderItems: [
        { product: { name: "Potato" }, quantity: 1 },
        { product: { name: "Potato" }, quantity: 1 }
      ]
    },
    {
      orderId: 9,
      status: "SHIPPED",
      orderDate: new Date(2023, 8, 21, 17, 46, 3).toISOString(),
      totalAmount: 2500,
      orderItems: [
        { product: { name: "Mixer" }, quantity: 1 }
      ]
    },
    {
      orderId: 3,
      status: "SHIPPED",
      orderDate: new Date(2023, 8, 18, 10, 2, 37).toISOString(),
      totalAmount: 23000,
      orderItems: [
        { product: { name: "Mixer" }, quantity: 1 },
        { product: { name: "Washing Machine" }, quantity: 1 }
      ]
    }
  ];
};

export const saveLocalOrders = (orders) => {
  try {
    localStorage.setItem("local_orders", JSON.stringify(orders));
  } catch (e) {
    console.error(e);
  }
};

export const getLocalProfile = () => {
  return {
    customerId: 1,
    name: localStorage.getItem("name") || "Vivek Gupta",
    email: "vivek@gmail.com",
    mobileNumber: "7894561236",
    registrationDate: "2023-09-18",
    status: "ACTIVE",
    address: [
      {
        addressId: 1,
        buildingName: "9 y 8",
        streetNo: "Patel Nagar",
        city: "Bhilwara",
        state: "Rajasthan",
        pincode: "311001"
      }
    ]
  };
};

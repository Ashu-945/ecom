import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Slider from "../components/Slider";
import "../comp_css/Home.css";

import Atta from "../picture/Atta_and_dals.avif";
import Beauty from "../picture/Beauty_and_personal_care.avif";
import Cleaning from "../picture/Cleaning_essentials.avif";
import Home_essentials from "../picture/Home_essentials.avif";
import kids_fashion from "../picture/kids_fashion.avif";
import Kitchen_must_haves from "../picture/Kitchen_must_haves.avif";
import Laptops_and_Tablets from "../picture/Laptops_and_Tablets.avif";
import men_fashion from "../picture/men_fashion.avif";
import Oil_and_ghee from "../picture/Oil_and_ghee.avif";
import Smart_Televisions from "../picture/Smart_Televisions.avif";

const Home = () => {
  const navigate = useNavigate();

  const varietyCards = [
    { img: Atta, category: "vegetables" },
    { img: Beauty, category: "beauty" },
    { img: Cleaning, category: "cleaning" },
    { img: Home_essentials, category: "home" },
    { img: kids_fashion, category: "fashion" },
    { img: Kitchen_must_haves, category: "electronics" },
    { img: Laptops_and_Tablets, category: "gadgets" },
    { img: men_fashion, category: "fashion" },
    { img: Oil_and_ghee, category: "vegetables" },
    { img: Smart_Televisions, category: "electronics" },
  ];

  const slideImages = [
    "https://www.jiomart.com/images/cms/aw_rbslider/slides/1691612739_Aaj_Ki_Deals_Desktop.jpg?im=Resize=(1680,320)",
    "https://www.jiomart.com/images/cms/aw_rbslider/slides/1691935239_Freedom_Finds.jpg?im=Resize=(1680,320)",
    "https://www.jiomart.com/images/cms/aw_rbslider/slides/1691950461_Handloom_Sarees_in_Colors_of_India.jpg?im=Resize=(1680,320)",
  ];

  const slideImages2 = [
    "https://www.jiomart.com/images/cms/aw_rbslider/slides/1691773740_Paytm_Wallet_1240x209.jpg?im=Resize=(1241,195)",
    "https://www.jiomart.com/images/cms/aw_rbslider/slides/1691435554_MobiKwik_1240x209.jpg?im=Resize=(1241,195)",
  ];

  useEffect(() => {
    document.title = "Ecommerce | Home Page";
  }, []);

  return (
    <div className="home-container">
      <div className="home-slider">
        <Slider images={slideImages} interval={4000} />
      </div>

      <div className="ImageFixed">
        <img
          src="https://www.jiomart.com/images/cms/aw_rbslider/slides/1691749079_Aaj_Ki_Deals.jpg?im=Resize=(1240,150)"
          alt="Aaj Ki Deals"
          onClick={() => navigate("/product")}
          style={{ cursor: "pointer" }}
        />
      </div>

      <div className="home-slider">
        <Slider images={slideImages2} interval={5000} />
      </div>

      <div className="cardbox">
        {varietyCards.map((item, index) => (
          <div
            key={index}
            className="cardbox-item"
            onClick={() => navigate(`/product?category=${item.category}`)}
            title={`View ${item.category} products`}
          >
            <img src={item.img} alt={`Category ${index}`} />
          </div>
        ))}
      </div>
    </div>
  );
};

export default Home;

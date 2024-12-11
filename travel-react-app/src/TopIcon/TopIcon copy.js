import React, { useState, useContext } from "react";
import { SlHome } from "react-icons/sl";
import { IoMapOutline } from "react-icons/io5";
import { BsFillPostageHeartFill } from "react-icons/bs";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../context/UserContext";
import PersonalInfo from "../pages/PersonalInfo";
import Modal from 'react-modal';
import '../css/MyPage.css';

Modal.setAppElement('#root');

const TopIcon = () => {

  const [logo, setLogo] = useState(null);
  const [isbutton, setIsbutton] = useState(false);
  const [isModalbutton, setIsModalbutton] = useState(true);

  const { profileImage } = useContext(UserContext);

  const handleLogoChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setLogo(URL.createObjectURL(file));
    }
  };
  const navigate = useNavigate();

  const iconComponents = [
    { id: "home", component: <SlHome />, route: "/main" },
    { id: "map", component: <IoMapOutline />, route: "/map" },
    { id: "post", component: <BsFillPostageHeartFill />, route: "/post" },
  ];

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    alert("로그아웃 되었습니다.");
    navigate('/login');
  };

  const [showPersonalInfo, setShowPersonalInfo] = useState(false);

  const openPersonalInfoModal = () => {
    setShowPersonalInfo(true); // Modal 열기
  };

  const closePersonalInfoModal = () => {
    setShowPersonalInfo(false); // Modal 닫기
  };

  return (
    <header className="home-header">
      <div className="logo-container">
        {logo ? (
          <img src={logo} alt="Logo" className="logo" />
        ) : (
          <label className="file-input">
            로고 선택
            <input type="file" accept="image/*" onChange={handleLogoChange} />
          </label>
        )}
      </div>
      <div className="icon-container">
        {iconComponents.map((icon) => (
          <div
            key={icon.id}
            className="icon"
            onClick={() => navigate(icon.route)}
          >
            {icon.component}
          </div>
        ))}
        <div style={{ width: "130px", height: "100px", 
                      justifyItems: "center", alignItems: "center", 
                      margin: "5px" }}>
          <img 
            style={{width: "50px",height:"50px",
                    borderRadius: "50%",cursor: "pointer", border:"solid 1px gray"}}
            src={profileImage} // 기본 이미지나 프로필 이미지 표시
            alt="profile"
            onClick={() => setIsbutton(!isbutton)}
          />
          <p>username</p>
          {isbutton && (
            <div className="profile_button">
              {isModalbutton &&(
                <button 
                style={{margin: "5px", padding: "10px", backgroundColor: "#007bff",
                  color: "white", border: "none", borderRadius: "5px"
                }}
                className="info-button"
                onClick={openPersonalInfoModal}
              >
                내 정보
              </button>
              )}
                {/* PersonalInfo Modal */}
                <Modal
                  isOpen={showPersonalInfo}
                  onRequestClose={closePersonalInfoModal}
                  contentLabel="Personal Information"
                  className="custom_modal"
                  overlayClassName="Overlay"
                >
                  <PersonalInfo />
                  <button onClick={closePersonalInfoModal}>닫기</button>
                </Modal>
              

              <button
                style={{margin: "5px", padding: "10px", backgroundColor: "#28a745",
                  color: "white", border: "none", borderRadius: "5px"
                }}
                onClick={() => navigate("/mypage/mypost")}
              >
                My게시글
              </button>
              <button 
                style={{margin: "5px", padding: "10px", backgroundColor: "rgb(212, 35, 35)",
                  color: "white", border: "none", borderRadius: "5px"
                }}
                onClick={handleLogout}>Logout</button>
              </div>
          )}
        </div>
      </div>
    </header>
  );
};

export default TopIcon;

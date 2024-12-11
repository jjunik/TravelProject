import React from "react";
import defaultImage from '../image/defaultImage.png';
import TopIcon from "../TopIcon/TopIcon";

const Logo = () => {
  return (
    <header style={styles.header}>
      {/* 로고 영역 */}
      <div style={styles.logoContainer}>
        <img 
          src={defaultImage} 
          alt="Logo" 
          style={styles.logoImage} 
        />
      </div>
    </header>
  );
};

const styles = {
  header: {
    display: "flex",
    justifyContent: "space-between", // 로고와 TopIcon을 양쪽으로 정렬
    alignItems: "center",           // 세로 중앙 정렬
    padding: "10px 20px",           // 헤더 안쪽 여백
    backgroundColor: "#f48626",         // 헤더 배경색
  },
  logoContainer: {
    display: "flex",
    alignItems: "center",           // 로고를 세로 중앙 정렬
  },
  logoImage: {
    width: "100px",                 // 로고 크기 조정
    height: "auto",                 // 비율 유지
  },
  topIconContainer: {
    display: "flex",
    alignItems: "center",           // TopIcon을 세로 중앙 정렬
  },
};

export default Logo;

import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomeScreen from "./pages/HomeScreen";
import MainScreen from "./pages/MainScreen";
import Login from "./pages/Login";
import Signup from "./pages/SignUp";
import PostDetail from "./pages/PostDetail";
import Post from "./pages/Post";
import Map from "./pages/Map";
import MyPage from "./pages/MyPage"; // Mypage 추가
import { PostContext } from "./context/PostContext";
import { UserContext } from "./context/UserContext";
import {PlaceContext} from "./context/PlaceContext";
import img1 from "./image/bhc.jpg";
import MapEdit from "./pages/MapEdit";
import { ListContext } from "./context/ListContext";
import { ImageContext } from "./context/ImageContext";
import { CopyListContext } from "./context/CopyListContext";
import Logo from './pages/Logo';


  
function App() {
  const [placeList, setPlaceList] = useState([]);
  const [list,setList] = useState([]);
  const [copyList,setCopyList] = useState([]);
  const [copyImage, setCopyImage] = useState([]);
  const [postList, setPostList] = useState([

  ]);

  const [user, setUser] = useState([
   
  
  ]);


  return (
    <UserContext.Provider value={{user,setUser }}>
      <PostContext.Provider value={{ postList, setPostList }}>
        <PlaceContext.Provider value={{placeList, setPlaceList}}>
          <ListContext.Provider value={{list, setList}}>
            <ImageContext.Provider value={{copyImage,setCopyImage}}>
              <CopyListContext.Provider value={{copyList,setCopyList}}>
                <div className="AppWrapper">
                <Router>
                  <Logo/>
                  <Routes>
                    <Route path="/" element={<HomeScreen />} />
                    <Route path="/main" element={<MainScreen />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/signup" element={<Signup />} />
                    <Route path="postdetail/:id" element={<PostDetail />} />
                    <Route path="post" element={<Post />} />
                    <Route path="postEdit/:id" element={<MapEdit />} />
                    <Route path="map" element={<Map />} />
                    <Route path="/mypage/*" element={<MyPage />} /> {/* Mypage 경로 추가 */}
                  </Routes>
                </Router>
              </div>
            </CopyListContext.Provider>
         </ImageContext.Provider>
        </ListContext.Provider>
      </PlaceContext.Provider>
    </PostContext.Provider>
    </UserContext.Provider>

  );
}

export default App;

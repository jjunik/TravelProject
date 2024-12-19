const PhotoUploader = () => {
    const [file, setFile] = useState(null);
    const [uploadedImages, setUploadedImages] = useState([]);

    // 파일 선택 이벤트
    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    // 서버로 파일 업로드
    const handleUpload = async () => {
        if (!file) {
            alert("파일을 선택하세요!");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await axios.post("http://localhost:9090/api/photos/upload", formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });
            setUploadedImages((prev) => [...prev, response.data.url]);
            alert("파일 업로드 성공!");
        } catch (error) {
            console.error("파일 업로드 실패:", error);
            alert("파일 업로드 실패!");
        }
    };

    // 서버에서 업로드된 이미지 불러오기
    const fetchUploadedImages = async () => {
        try {
            const response = await axios.get("http://localhost:9090/api/photos");
            setUploadedImages(response.data);
        } catch (error) {
            console.error("이미지 가져오기 실패:", error);
        }
    };

    // 서버로 삭제 요청
    const handleDelete = async (url) => {
        try {
            await axios.delete("http://localhost:9090/api/photos/delete", { data: { url } });
            setUploadedImages((prev) => prev.filter((image) => image !== url)); // 삭제된 이미지 제거
            alert("파일 삭제 성공!");
        } catch (error) {
            console.error("파일 삭제 실패:", error);
            alert("파일 삭제 실패!");
        }
    };

    useEffect(() => {
        fetchUploadedImages();
    }, []);

    return (
        <div>
            <h1>사진 업로드</h1>
            <input type="file" onChange={handleFileChange} />
            <button onClick={handleUpload}>업로드</button>

            <h2>업로드된 이미지</h2>
            <div style={{ display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gap: "10px" }}>
                {uploadedImages.map((url, idx) => (
                    <div key={idx} style={{ border: "1px solid #ddd", borderRadius: "5px", overflow: "hidden" }}>
                        <img src={url} alt={`Uploaded ${idx}`} style={{ width: "100%", height: "150px", objectFit: "cover" }} />
                        <button onClick={() => handleDelete(url)} style={{ width: "100%" }}>
                            삭제
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default PhotoUploader;

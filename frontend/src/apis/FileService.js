import axiosClient from "./AxiosClient.js";

export const FileService = {
    uploadFile: (file) => {
        const formData = new FormData();
        formData.append('file', file);
        return axiosClient.post('/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    },

    deleteImage: (imageUrl) => {
        return axiosClient.delete(`/upload?imageUrl=${encodeURIComponent(imageUrl)}`);
    }
}
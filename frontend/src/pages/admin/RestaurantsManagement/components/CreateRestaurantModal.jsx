import React, {useRef, useState} from "react";
import {UploadCloud, X} from "lucide-react";
import { Input } from "../../../../components/common/Input";
import { Button } from "../../../../components/common/Button";
import {Select} from "../../../../components/common/Select.jsx";
import {FileService} from "../../../../apis/FileService.js";

export default function CreateRestaurantModal({ isOpen, onClose, handleCreate, owners }) {
    const [formData, setFormData] = useState({
        name: '', address: '', phoneNumber: '', description: '', imageUrl: '', ownerId: ''
    });

    const [imageFile, setImageFile] = useState(null);
    const [imagePreview, setImagePreview] = useState(null);
    const [isUploading, setIsUploading] = useState(false);

    const fileInputRef = useRef(null);

    if (!isOpen) return null;

    const handleFileChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setImageFile(file);
            setImagePreview(URL.createObjectURL(file));
        }
    };

    const onSubmit = async () => {
        if (!formData.name || !formData.address || !formData.ownerId) {
            alert("Please fill in the required fields!");
            return;
        }

        setIsUploading(true);
        let finalImageUrl = "";

        try {
            if (imageFile) {
                const uploadRes = await FileService.uploadFile(imageFile);
                finalImageUrl = uploadRes.data.url;
            }
            try {
                await handleCreate({
                    ...formData,
                    imageUrl: finalImageUrl
                });

                setImageFile(null);
                setImagePreview(null);
                setFormData({ name: '', address: '', phoneNumber: '', description: '', ownerId: '' });

            } catch (createError) {
                console.error("Error creating restaurant, proceed to clean up junk photos...");
                if (finalImageUrl) {
                    await FileService.deleteImage(finalImageUrl);
                    console.log("Junk images have been removed from Cloudinary!");
                }
                throw createError;
            }

        } catch (error) {
            console.error("Error uploading image file or creating restaurant", error);
            alert("An error occurred, please try again.");
        } finally {
            setIsUploading(false);
        }
    };

    return (
        <div className="modal">
            <div className="modal-overlay">
                <div className="modal-content">
                    <div className="modal-header">
                        <h2>Create new restaurant</h2>
                        <button className="close-btn" onClick={onClose}><X size={20}/></button>
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                        <Input placeholder="Restaurant name (*)" value={formData.name} onChange={(e) => setFormData({...formData, name: e.target.value})} />

                        <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
                            <span style={{ fontSize: '14px', color: 'var(--foreground)', fontWeight: 500 }}>Restaurant owner (*)</span>
                            <Select
                                options={[
                                    { value: '', label: '--- Choose Owner ---' },
                                    ...owners
                                ]}
                                value={formData.ownerId}
                                onChange={(e) => setFormData({...formData, ownerId: e.target.value})}
                            />
                            {owners.length === 0 && (
                                <span style={{ fontSize: '12px', color: 'var(--destructive)' }}>
                                * Note: You must create the RESTAURANT role account in the Users tab first!
                            </span>
                            )}
                        </div>

                        <Input placeholder="Address (*)" value={formData.address} onChange={(e) => setFormData({...formData, address: e.target.value})} />
                        <Input placeholder="Phone number" value={formData.phoneNumber} onChange={(e) => setFormData({...formData, phoneNumber: e.target.value})} />
                        <Input placeholder="Description" value={formData.description} onChange={(e) => setFormData({...formData, description: e.target.value})} />

                        <div className="image-upload-container">
                            <span style={{ fontSize: '14px', color: 'var(--foreground)', fontWeight: 500, marginBottom: '8px', display: 'block' }}>Restaurant image</span>

                            <div
                                className="upload-dropzone"
                                onClick={() => fileInputRef.current.click()}
                            >
                                {imagePreview ? (
                                    <img src={imagePreview} alt="Preview" className="upload-preview-img" />
                                ) : (
                                    <div className="upload-placeholder">
                                        <UploadCloud size={32} className="text-muted" />
                                        <p>Click to select an image</p>
                                        <span className="text-xs text-muted">PNG, JPG, GIF max 5MB</span>
                                    </div>
                                )}
                                <input
                                    type="file"
                                    ref={fileInputRef}
                                    onChange={handleFileChange}
                                    accept="image/png, image/jpeg, image/gif"
                                    style={{ display: 'none' }}
                                />
                            </div>
                            {imagePreview && (
                                <div style={{ textAlign: 'center', marginTop: '8px' }}>
                                <span className="text-sm text-destructive" style={{ cursor: 'pointer' }} onClick={() => { setImageFile(null); setImagePreview(null); }}>
                                    Delete image
                                </span>
                                </div>
                            )}
                        </div>
                    </div>

                    <div className="modal-actions" style={{ marginTop: '16px' }}>
                        <Button className="btn-outline flex-1" onClick={onClose}>Cancel</Button>
                        <Button className="btn-primary flex-1" onClick={onSubmit}>Submit</Button>
                    </div>
                </div>
            </div>
        </div>
    );
}
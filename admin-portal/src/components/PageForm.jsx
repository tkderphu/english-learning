import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Stack,
  IconButton,
  CircularProgress,
  Box,
  Typography
} from '@mui/material';
import { Close as CloseIcon, Audiotrack as AudioIcon } from '@mui/icons-material';
import api from '../api/axios';

const PageForm = ({ open, handleClose, chapterId, refresh }) => {
  const [formData, setFormData] = useState({
    chapterId: parseInt(chapterId),
    content: '',
    audioId: 0,
    number: 1
  });
  const [loading, setLoading] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [audioUrl, setAudioUrl] = useState('');

  useEffect(() => {
    if (open) {
      setFormData({
        chapterId: parseInt(chapterId),
        content: '',
        audioId: 0,
        number: 1
      });
      setAudioUrl('');
    }
  }, [open, chapterId]);

  const handleAudioUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setUploading(true);
    const uploadData = new FormData();
    uploadData.append('file', file);

    try {
      const resp = await api.post('/audio/v1/upload', uploadData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      
      if (resp.statusCode === 200) {
        setFormData(prev => ({ ...prev, audioId: resp.data.id }));
        setAudioUrl(resp.data.fileUrl);
        alert('Audio uploaded successfully! ID: ' + resp.data.id);
      }
    } catch (error) {
      console.error('Upload failed', error);
    } finally {
      setUploading(false);
    }
  };

  const handleSubmit = async () => {
    setLoading(true);
    try {
      await api.post('/page/v1', formData);
      refresh();
      handleClose();
    } catch (error) {
      console.error('Submit failed', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
      <DialogTitle sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        Thêm Trang mới
        <IconButton onClick={handleClose} size="small"><CloseIcon /></IconButton>
      </DialogTitle>
      <DialogContent dividers>
        <Stack spacing={3} sx={{ mt: 1 }}>
          <Box sx={{ p: 2, border: '1px dashed #cbd5e1', borderRadius: '8px', textAlign: 'center', bgcolor: '#f8fafc' }}>
            <Typography variant="subtitle2" gutterBottom>1. Tải lên Âm thanh trước</Typography>
            <input
              accept="audio/*"
              style={{ display: 'none' }}
              id="page-audio-upload"
              type="file"
              onChange={handleAudioUpload}
            />
            <label htmlFor="page-audio-upload">
              <Button
                variant="outlined"
                component="span"
                startIcon={uploading ? <CircularProgress size={20} /> : <AudioIcon />}
                disabled={uploading}
                sx={{ borderRadius: '10px' }}
              >
                {uploading ? 'Đang tải lên...' : 'Chọn tệp Âm thanh'}
              </Button>
            </label>
            {audioUrl && (
              <Typography variant="caption" display="block" sx={{ mt: 1, color: 'success.main', wordBreak: 'break-all' }}>
                ID Âm thanh: {formData.audioId} | Đường dẫn: {audioUrl}
              </Typography>
            )}
          </Box>

          <Typography variant="subtitle2">2. Chi tiết Trang</Typography>

          <TextField
            label="Số thứ tự Trang"
            type="number"
            value={formData.number}
            onChange={(e) => setFormData({ ...formData, number: parseInt(e.target.value) })}
            fullWidth
            required
            size="small"
          />
          
{/*           <TextField */}
{/*             label="Nội dung" */}
{/*             value={formData.content} */}
{/*             onChange={(e) => setFormData({ ...formData, content: e.target.value })} */}
{/*             fullWidth */}
{/*             required */}
{/*             multiline */}
{/*             rows={6} */}
{/*             size="small" */}
{/*             placeholder="Nhập nội dung văn bản của trang tại đây..." */}
{/*           /> */}
        </Stack>
      </DialogContent>
      <DialogActions sx={{ p: 2, bgcolor: '#f8fafc' }}>
        <Button onClick={handleClose} color="inherit">Hủy</Button>
        <Button
          onClick={handleSubmit}
          variant="contained"
          disabled={loading || !formData.content || formData.audioId === 0}
          startIcon={loading && <CircularProgress size={20} />}
          sx={{ 
            borderRadius: '10px',
            backgroundColor: '#3b82f6',
            '&:hover': { backgroundColor: '#2563eb' },
            '&.Mui-disabled': {
              backgroundColor: 'rgba(59, 130, 246, 0.5)',
              color: '#ffffff'
            }
          }}
        >
          Tạo Trang
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default PageForm;

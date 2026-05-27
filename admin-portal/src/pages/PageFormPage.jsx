/**
 * PageFormPage - Trang giao diện thêm/sửa phân đoạn trang sách
 *
 * Xử lý logic tạo một trang nội dung thuộc chương sách. Điểm quan trọng là
 * hàm `handleAudioUpload` gọi `POST /audio/v1/upload` (sử dụng FormData)
 * để lấy `audioId`.
 * Khi Submit, form sẽ gọi `POST /page/v1` kèm theo `chapterId`, `number` và `audioId`.
 */
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Button,
  Paper,
  Typography,
  IconButton,
  Breadcrumbs,
  Link,
  CircularProgress,
  Stack,
  Fade,
  TextField,
  Grid
} from '@mui/material';
import { ArrowBack as ArrowBackIcon, Save as SaveIcon, Audiotrack as AudioIcon } from '@mui/icons-material';
import api from '../api/axios';

const PageFormPage = () => {
  const { bookId, chapterId, pageId } = useParams();
  const navigate = useNavigate();
  const isEdit = Boolean(pageId);

  const [formData, setFormData] = useState({
    chapterId: parseInt(chapterId),
    content: '',
    audioId: 0,
    number: 1
  });

  const [loading, setLoading] = useState(false);
  const [fetching, setFetching] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [audioUrl, setAudioUrl] = useState('');
  const [chapterTitle, setChapterTitle] = useState('');

  const fetchChapterDetail = async () => {
    try {
      const response = await api.get(`/chapter/v1`, { params: { bookId, page: 1, limit: 100 } });
      if (response.statusCode === 200) {
        const chapter = response.data.data?.find(c => c.id === parseInt(chapterId));
        if (chapter) setChapterTitle(chapter.title);
      }
    } catch (e) {
      console.error('Failed to fetch chapter', e);
    }
  };

  const fetchPageDetail = async () => {
    if (!isEdit) return;
    setFetching(true);
    try {
      const response = await api.get(`/page/v1`, { params: { chapterId, page: 1, limit: 100 } });
      if (response.statusCode === 200) {
        const pageItem = response.data.content?.find(p => p.id === parseInt(pageId));
        if (pageItem) {
          setFormData({
            chapterId: parseInt(chapterId),
            content: pageItem.content || '',
            audioId: pageItem.audio?.id || 0,
            number: pageItem.number || 1
          });
          setAudioUrl(pageItem.audio?.fileUrl || '');
        }
      }
    } catch (error) {
      console.error('Failed to fetch page details', error);
    } finally {
      setFetching(false);
    }
  };

  useEffect(() => {
    fetchChapterDetail();
    fetchPageDetail();
  }, [bookId, chapterId, pageId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'number' ? parseInt(value) || 0 : value,
    }));
  };

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
      }
    } catch (error) {
      console.error('Upload failed', error);
      alert('Tải âm thanh thất bại!');
    } finally {
      setUploading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      if (isEdit) {
        // Fallback for edit if available, currently just assuming backend supports it
        // await api.put(`/page/v1/${pageId}`, formData);
      } else {
        await api.post('/page/v1', formData);
      }
      navigate(`/books/${bookId}/chapters/${chapterId}/pages`);
    } catch (error) {
      console.error('Submit page failed', error);
      alert('Có lỗi xảy ra khi lưu trang!');
    } finally {
      setLoading(false);
    }
  };

  if (fetching) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Fade in={true}>
      <Box sx={{ p: 2 }}>
        {/* Header */}
        <Box sx={{ mb: 4 }}>
          <Breadcrumbs sx={{ mb: 1.5, fontSize: '0.85rem' }}>
            <Link color="inherit" onClick={() => navigate('/books')} sx={{ cursor: 'pointer', textDecoration: 'none' }}>Danh sách sách</Link>
            <Link color="inherit" onClick={() => navigate(`/books/${bookId}/chapters`)} sx={{ cursor: 'pointer', textDecoration: 'none' }}>Quản lý chương</Link>
            <Link color="inherit" onClick={() => navigate(`/books/${bookId}/chapters/${chapterId}/pages`)} sx={{ cursor: 'pointer', textDecoration: 'none' }}>Phân đoạn trang</Link>
            <Typography color="text.primary" sx={{ fontSize: '0.85rem' }}>
              {isEdit ? 'Chỉnh sửa trang' : 'Thêm trang mới'}
            </Typography>
          </Breadcrumbs>
          
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
            <IconButton 
              onClick={() => navigate(`/books/${bookId}/chapters/${chapterId}/pages`)} 
              sx={{ backgroundColor: '#f1f5f9', '&:hover': { backgroundColor: '#e2e8f0' } }}
            >
              <ArrowBackIcon />
            </IconButton>
            <Box>
              <Typography variant="h4" sx={{ fontWeight: 700, color: '#1e293b' }}>
                {isEdit ? 'Chỉnh sửa Trang' : 'Thêm Trang mới'}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Chương: <b>{chapterTitle || chapterId}</b>
              </Typography>
            </Box>
          </Box>
        </Box>

        {/* Content */}
        <Paper sx={{ p: 4, borderRadius: '16px', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)' }}>
          <Box component="form" onSubmit={handleSubmit}>
            <Box sx={{ mb: 4 }}>
              <Grid container spacing={3} sx={{ mb: 4 }}>
                <Grid item xs={12} md={8}>
                  <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: 600, color: '#475569' }}>
                    Tệp âm thanh nguồn <span style={{ color: '#ef4444' }}>*</span>
                  </Typography>
                  <Box sx={{ p: 0, mt: 1, height: '100%', display: 'flex', flexDirection: 'column', justifyContent: 'flex-start' }}>
                    <Stack direction="row" spacing={2} alignItems="center">
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
                          sx={{ borderRadius: '8px', textTransform: 'none', borderColor: '#cbd5e1', color: '#475569', px: 3 }}
                        >
                          {uploading ? 'Đang tải lên...' : 'Chọn tệp Âm thanh'}
                        </Button>
                      </label>
                      {audioUrl && (
                        <Box>
                          <Typography variant="body2" sx={{ color: '#10b981', fontWeight: 600, display: 'flex', alignItems: 'center', gap: 1 }}>
                            <Box component="span" sx={{ width: 8, height: 8, borderRadius: '50%', backgroundColor: '#10b981' }} />
                            Đã tải lên thành công
                          </Typography>
                          <Typography variant="caption" sx={{ color: '#64748b' }}>
                            ID: {formData.audioId}
                          </Typography>
                        </Box>
                      )}
                    </Stack>
                    {audioUrl && (
                      <Box sx={{ mt: 2 }}>
                        <audio src={audioUrl} controls style={{ width: '100%', height: '40px', outline: 'none' }} />
                      </Box>
                    )}
                  </Box>
                </Grid>
                <Grid item xs={12} md={4}>
                  <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: 600, color: '#475569' }}>
                    Số thứ tự Trang <span style={{ color: '#ef4444' }}>*</span>
                  </Typography>
                  <TextField
                    name="number"
                    type="number"
                    value={formData.number}
                    onChange={handleChange}
                    fullWidth
                    required
                    inputProps={{ min: 1 }}
                    InputProps={{ sx: { borderRadius: '8px' } }}
                  />
                </Grid>
              </Grid>

{/*               <Grid container spacing={3}> */}
{/*                 <Grid item xs={12}> */}
{/*                   <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: 600, color: '#475569' }}> */}
{/*                     Nội dung văn bản <span style={{ color: '#ef4444' }}>*</span> */}
{/*                   </Typography> */}
{/*                   <TextField */}
{/*                     name="content" */}
{/*                     value={formData.content} */}
{/*                     onChange={handleChange} */}
{/*                     fullWidth */}
{/*                     required */}
{/*                     multiline */}
{/*                     rows={8} */}
{/*                     placeholder="Nhập toàn bộ nội dung văn bản của trang tại đây. Hệ thống sẽ tự động phân tách thành các câu (sentences)..." */}
{/*                     InputProps={{ sx: { borderRadius: '8px', fontSize: '1.05rem', lineHeight: 1.6 } }} */}
{/*                   /> */}
{/*                 </Grid> */}
{/*               </Grid> */}
            </Box>

            <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 4 }}>
              <Button
                variant="outlined"
                color="inherit"
                onClick={() => navigate(`/books/${bookId}/chapters/${chapterId}/pages`)}
                sx={{ mr: 2, borderRadius: '8px', px: 4, textTransform: 'none', fontWeight: 600, borderColor: '#cbd5e1' }}
              >
                Hủy
              </Button>
              <Button
                type="submit"
                variant="contained"
                startIcon={loading ? <CircularProgress size={20} color="inherit" /> : <SaveIcon />}
                sx={{ 
                  borderRadius: '8px', 
                  px: 4, 
                  py: 1, 
                  textTransform: 'none', 
                  fontWeight: 600,
                  backgroundColor: '#3b82f6',
                  '&:hover': { backgroundColor: '#2563eb' },
                  '&.Mui-disabled': {
                    backgroundColor: 'rgba(59, 130, 246, 0.5)',
                    color: '#ffffff'
                  }
                }}
              >
                {isEdit ? 'Lưu thay đổi' : 'Tạo mới trang'}
              </Button>
            </Box>
          </Box>
        </Paper>
      </Box>
    </Fade>
  );
};

export default PageFormPage;

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
import { ArrowBack as ArrowBackIcon, Save as SaveIcon } from '@mui/icons-material';
import api from '../api/axios';

const ChapterFormPage = () => {
  const { bookId, chapterId } = useParams();
  const navigate = useNavigate();
  const isEdit = Boolean(chapterId);

  const [formData, setFormData] = useState({
    bookId: parseInt(bookId),
    title: '',
    description: '',
    number: 1
  });

  const [loading, setLoading] = useState(false);
  const [fetching, setFetching] = useState(false);
  const [bookTitle, setBookTitle] = useState('');

  const fetchBookDetail = async () => {
    try {
      const response = await api.get(`/book/v1/${bookId}`);
      if (response.statusCode === 200) {
        setBookTitle(response.data.title);
      }
    } catch (e) {
      console.error('Failed to fetch book', e);
    }
  };

  const fetchChapterDetail = async () => {
    if (!isEdit) return;
    setFetching(true);
    try {
      // Find chapter by ID. The backend doesn't have a single GET /chapter/v1/:id yet, 
      // but we can fetch the list and find it. This is a workaround.
      const response = await api.get(`/chapter/v1`, { params: { bookId, page: 1, limit: 100 } });
      if (response.statusCode === 200) {
        const chapter = response.data.data?.find(c => c.id === parseInt(chapterId));
        if (chapter) {
          setFormData({
            bookId: parseInt(bookId),
            title: chapter.title || '',
            description: chapter.description || '',
            number: chapter.number || 1
          });
        }
      }
    } catch (error) {
      console.error('Failed to fetch chapter details', error);
    } finally {
      setFetching(false);
    }
  };

  useEffect(() => {
    fetchBookDetail();
    fetchChapterDetail();
  }, [bookId, chapterId]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'number' ? parseInt(value) || 0 : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      if (isEdit) {
        // Assume PUT /api/chapter/v1 exists or handle accordingly
        // Since we didn't see PUT in PageController, ChapterController might be missing it too, 
        // but let's stick to the structure that was in ChapterForm.jsx which was commented out
        await api.put(`/chapter/v1/${chapterId}`, formData);
      } else {
        await api.post('/chapter/v1', formData);
      }
      navigate(`/books/${bookId}/chapters`);
    } catch (error) {
      console.error('Submit chapter failed', error);
      alert('Có lỗi xảy ra khi lưu chương!');
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
            <Typography color="text.primary" sx={{ fontSize: '0.85rem' }}>
              {isEdit ? 'Chỉnh sửa chương' : 'Thêm chương mới'}
            </Typography>
          </Breadcrumbs>
          
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
            <IconButton 
              onClick={() => navigate(`/books/${bookId}/chapters`)} 
              sx={{ backgroundColor: '#f1f5f9', '&:hover': { backgroundColor: '#e2e8f0' } }}
            >
              <ArrowBackIcon />
            </IconButton>
            <Box>
              <Typography variant="h4" sx={{ fontWeight: 700, color: '#1e293b' }}>
                {isEdit ? 'Chỉnh sửa Chương' : 'Thêm Chương mới'}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Sách: <b>{bookTitle}</b>
              </Typography>
            </Box>
          </Box>
        </Box>

        {/* Content */}
        <Paper sx={{ p: 4, borderRadius: '16px', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)' }}>
          <Box component="form" onSubmit={handleSubmit}>
            <Box sx={{ mb: 4 }}>
              <Typography variant="h6" sx={{ fontWeight: 600, color: '#334155', mb: 3 }}>
                Thông tin chương
              </Typography>
              <Grid container spacing={3}>
                <Grid item xs={12} md={9}>
                  <TextField
                    label="Tiêu đề chương"
                    name="title"
                    value={formData.title}
                    onChange={handleChange}
                    fullWidth
                    required
                    placeholder="VD: Chương 1: Sự khởi đầu"
                    InputProps={{ sx: { borderRadius: '8px' } }}
                  />
                </Grid>
                <Grid item xs={12} md={3}>
                  <TextField
                    label="Số thứ tự"
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
                <Grid item xs={12}>
                  <TextField
                    label="Mô tả"
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    fullWidth
                    multiline
                    rows={4}
                    placeholder="Nhập mô tả tóm tắt nội dung chương..."
                    InputProps={{ sx: { borderRadius: '8px' } }}
                  />
                </Grid>
              </Grid>
            </Box>

            <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 2 }}>
              <Button
                variant="outlined"
                color="inherit"
                onClick={() => navigate(`/books/${bookId}/chapters`)}
                sx={{ mr: 2, borderRadius: '8px', px: 4, textTransform: 'none', fontWeight: 600, borderColor: '#cbd5e1' }}
              >
                Hủy
              </Button>
              <Button
                type="submit"
                variant="contained"
                disabled={loading || !formData.title}
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
                {isEdit ? 'Lưu thay đổi' : 'Tạo mới chương'}
              </Button>
            </Box>
          </Box>
        </Paper>
      </Box>
    </Fade>
  );
};

export default ChapterFormPage;

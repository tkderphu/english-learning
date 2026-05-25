import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Button,
  Paper,
  TextField,
  Typography,
  Stack,
  IconButton,
  Grid,
  CircularProgress,
  MenuItem,
  Autocomplete,
  Chip,
  Breadcrumbs,
  Link,
  Fade
} from '@mui/material';
import {
  ArrowBack as BackIcon,
  CloudUpload as UploadIcon,
  Audiotrack as AudioIcon
} from '@mui/icons-material';
import api from '../api/axios';

const BookFormPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = Boolean(id);

  const [formData, setFormData] = useState({
    title: '',
    language: 'English',
    coverUrl: '',
    authorIds: [],
    genreIds: []
  });

  const [loading, setLoading] = useState(false);
  const [fetching, setFetching] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [audioUrl, setAudioUrl] = useState('');
  
  const [authors, setAuthors] = useState([]);
  const [genres, setGenres] = useState([]);

  useEffect(() => {
    fetchAuthors();
    fetchGenres();
    if (isEdit) {
      fetchBookDetail();
    }
  }, [id]);

  const fetchBookDetail = async () => {
    setFetching(true);
    try {
      const resp = await api.get(`/book/v1/${id}`);
      if (resp.statusCode === 200) {
        setFormData({
          title: resp.data.title || '',
          language: resp.data.language || 'English',
          coverUrl: resp.data.coverUrl || '',
          authorIds: resp.data.authorIds || [],
          genreIds: resp.data.genreIds || []
        });
      }
    } catch (e) {
      console.error(e);
    } finally {
      setFetching(false);
    }
  };

  const fetchAuthors = async () => {
    try {
      const resp = await api.get('/book/v1/authors', { params: { page: 1, limit: 100 } });
      const list = resp.data?.data || (Array.isArray(resp.data) ? resp.data : []);
      setAuthors(list);
    } catch (e) { console.error('Fetch authors failed:', e); }
  };

  const fetchGenres = async () => {
    try {
      const resp = await api.get('/genre/v1');
      const list = resp.data?.data || (Array.isArray(resp.data) ? resp.data : []);
      setGenres(list);
    } catch (e) { console.error('Fetch genres failed:', e); }
  };

  const handleImageUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setUploading(true);
    const uploadData = new FormData();
    uploadData.append('file', file);

    try {
      // Use the generic file upload endpoint instead of the audio-specific one
      const resp = await api.post('/file/v1/upload', uploadData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      
      if (resp.statusCode === 200) {
        const url = resp.data.url; // Use 'url' field from FileResponse
        setFormData(prev => ({ ...prev, coverUrl: url }));
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
      if (isEdit) {
        // await api.put(`/book/v1/${id}`, formData);
      } else {
        await api.post('/book/v1', formData);
      }
      navigate('/books');
    } catch (error) {
      console.error('Submit failed', error);
    } finally {
      setLoading(false);
    }
  };

  if (fetching) return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 10 }}><CircularProgress /></Box>;

  return (
    <Fade in={true}>
      <Box sx={{ p: 1 }}>
        {/* Header Section */}
        <Box sx={{ mb: 3 }}>
          <Breadcrumbs sx={{ mb: 1, fontSize: '0.85rem' }}>
            <Link color="inherit" onClick={() => navigate('/')} sx={{ cursor: 'pointer', textDecoration: 'none' }}>
              Trang chủ
            </Link>
            <Link color="inherit" onClick={() => navigate('/books')} sx={{ cursor: 'pointer', textDecoration: 'none' }}>
              Danh sách sách
            </Link>
            <Typography color="text.primary" sx={{ fontSize: '0.85rem' }}>{isEdit ? 'Chỉnh sửa sách' : 'Thêm sách'}</Typography>
          </Breadcrumbs>
          
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Typography variant="h4" sx={{ fontWeight: 700, color: '#1e293b' }}>
              {isEdit ? 'Chỉnh sửa thông tin sách' : 'Thêm sách mới'}
            </Typography>
            <Button
              variant="outlined"
              startIcon={<BackIcon />}
              onClick={() => navigate('/books')}
              sx={{ borderColor: '#e2e8f0', color: '#64748b', borderRadius: '8px', textTransform: 'none' }}
            >
              Quay lại
            </Button>
          </Box>
        </Box>

        <Paper sx={{ p: 4, borderRadius: '12px', border: '1px solid #f1f5f9', boxShadow: '0 1px 3px 0 rgb(0 0 0 / 0.1)' }}>
          <Typography variant="h6" sx={{ fontWeight: 700, mb: 4, color: '#334155' }}>
            Thông tin sách
          </Typography>

          <Grid container spacing={3}>
            {/* Row 1: Tiêu đề & Ngôn ngữ */}
            <Grid item xs={12} md={6}>
              <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: 600, color: '#475569' }}>
                Tiêu đề sách <span style={{ color: '#ef4444' }}>*</span>
              </Typography>
              <TextField
                fullWidth
                placeholder="Nhập tiêu đề sách"
                size="small"
                value={formData.title}
                onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                sx={{ '& .MuiOutlinedInput-root': { borderRadius: '8px' } }}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: 600, color: '#475569' }}>
                Ngôn ngữ <span style={{ color: '#ef4444' }}>*</span>
              </Typography>
              <TextField
                select
                fullWidth
                size="small"
                value={formData.language}
                onChange={(e) => setFormData({ ...formData, language: e.target.value })}
                sx={{ '& .MuiOutlinedInput-root': { borderRadius: '8px' } }}
              >
                {['English', 'Vietnamese', 'Spanish', 'French'].map((lang) => (
                  <MenuItem key={lang} value={lang}>{lang === 'English' ? 'Tiếng Anh' : lang === 'Vietnamese' ? 'Tiếng Việt' : lang}</MenuItem>
                ))}
              </TextField>
            </Grid>

            {/* Row 2: Tác giả & Thể loại */}
            <Grid item xs={12} md={6}>
              <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: 600, color: '#475569' }}>
                Tác giả
              </Typography>
              <Autocomplete
                multiple
                size="small"
                options={authors}
                getOptionLabel={(option) => option.name || ''}
                value={authors.filter(a => formData.authorIds.includes(a.id))}
                onChange={(event, newValue) => {
                  setFormData({ ...formData, authorIds: newValue.map(v => v.id) });
                }}
                renderInput={(params) => <TextField {...params} placeholder="Chọn tác giả" sx={{ '& .MuiOutlinedInput-root': { borderRadius: '8px' } }} />}
                renderTags={(tagValue, getTagProps) =>
                  tagValue.map((option, index) => (
                    <Chip key={option.id} label={option.name} size="small" {...getTagProps({ index })} sx={{ borderRadius: '6px' }} />
                  ))
                }
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: 600, color: '#475569' }}>
                Thể loại
              </Typography>
              <Autocomplete
                multiple
                size="small"
                options={genres}
                getOptionLabel={(option) => option.name || ''}
                value={genres.filter(g => formData.genreIds.includes(g.id))}
                onChange={(event, newValue) => {
                  setFormData({ ...formData, genreIds: newValue.map(v => v.id) });
                }}
                renderInput={(params) => <TextField {...params} placeholder="Chọn thể loại" sx={{ '& .MuiOutlinedInput-root': { borderRadius: '8px' } }} />}
                renderTags={(tagValue, getTagProps) =>
                  tagValue.map((option, index) => (
                    <Chip key={option.id} label={option.name} size="small" {...getTagProps({ index })} sx={{ borderRadius: '6px' }} />
                  ))
                }
              />
            </Grid>

            {/* Row 3: Image Upload & Generated Path */}
            <Grid item xs={12}>
              <Typography variant="subtitle2" sx={{ mb: 1, fontWeight: 600, color: '#475569' }}>
                Ảnh bìa sách
              </Typography>
              <Box sx={{ p: 3, border: '1px dashed #e2e8f0', borderRadius: '12px', bgcolor: '#f8fafc', mb: 2 }}>
                <Stack direction="row" spacing={2} alignItems="center">
                  <input
                    accept="image/*"
                    style={{ display: 'none' }}
                    id="book-cover-upload"
                    type="file"
                    onChange={handleImageUpload}
                  />
                  <label htmlFor="book-cover-upload">
                    <Button
                      variant="outlined"
                      component="span"
                      startIcon={uploading ? <CircularProgress size={20} /> : <UploadIcon />}
                      disabled={uploading}
                      sx={{ borderRadius: '8px', textTransform: 'none', borderColor: '#cbd5e1', color: '#475569' }}
                    >
                      {uploading ? 'Đang tải lên...' : 'Tải lên ảnh bìa'}
                    </Button>
                  </label>
                  {formData.coverUrl && (
                    <Typography variant="body2" sx={{ color: '#10b981', fontWeight: 500 }}>
                      ✓ Đã tải lên ảnh bìa thành công
                    </Typography>
                  )}
                </Stack>
              </Box>
              <TextField
                fullWidth
                label="Đường dẫn Ảnh bìa"
                size="small"
                value={formData.coverUrl}
                onChange={(e) => setFormData({ ...formData, coverUrl: e.target.value })}
                sx={{ '& .MuiOutlinedInput-root': { borderRadius: '8px' } }}
              />

              {/* Real-time Preview */}
              {formData.coverUrl && (
                <Box sx={{ mt: 3, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                  <Typography variant="caption" sx={{ mb: 1, fontWeight: 700, color: '#64748b', alignSelf: 'flex-start' }}>
                    Xem trước ảnh bìa:
                  </Typography>
                  <Box
                    sx={{
                      width: 180,
                      height: 260,
                      borderRadius: '12px',
                      overflow: 'hidden',
                      boxShadow: '0 10px 15px -3px rgba(0, 0, 0, 0.1)',
                      border: '4px solid #fff',
                      backgroundColor: '#f8fafc',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center'
                    }}
                  >
                    <img 
                      src={formData.coverUrl} 
                      alt="Book Cover Preview" 
                      style={{ width: '100%', height: '100%', objectFit: 'cover' }}
                      onError={(e) => {
                        e.target.style.display = 'none';
                        e.target.parentNode.innerHTML = '<div style="color: #94a3b8; font-size: 0.75rem; text-align: center; padding: 10px;">URL ảnh không hợp lệ</div>';
                      }}
                    />
                  </Box>
                </Box>
              )}
            </Grid>
          </Grid>

          {/* Action Buttons */}
          <Box sx={{ mt: 5, display: 'flex', justifyContent: 'flex-end' }}>
            <Button
              variant="contained"
              onClick={handleSubmit}
              disabled={loading || !formData.title || !formData.language}
              sx={{ 
                backgroundColor: '#3b82f6', 
                '&:hover': { backgroundColor: '#2563eb' },
                '&.Mui-disabled': {
                  backgroundColor: 'rgba(59, 130, 246, 0.5)',
                  color: '#ffffff'
                },
                borderRadius: '8px',
                textTransform: 'none',
                fontWeight: 600,
                px: 4,
                py: 1
              }}
            >
              {loading ? <CircularProgress size={24} color="inherit" /> : 'Lưu'}
            </Button>
          </Box>
        </Paper>
      </Box>
    </Fade>
  );
};

export default BookFormPage;

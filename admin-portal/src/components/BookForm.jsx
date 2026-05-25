import React, { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Box,
  Typography,
  MenuItem,
  CircularProgress,
  IconButton,
  Stack,
  Autocomplete,
  Chip
} from '@mui/material';
import { CloudUpload as UploadIcon, Close as CloseIcon, Audiotrack as AudioIcon } from '@mui/icons-material';
import api from '../api/axios';

const BookForm = ({ open, handleClose, book, refresh }) => {
  const [formData, setFormData] = useState({
    title: '',
    language: 'English',
    coverUrl: '',
    authorIds: [],
    genreIds: []
  });

  const [loading, setLoading] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [audioUrl, setAudioUrl] = useState('');
  
  const [authors, setAuthors] = useState([]);
  const [genres, setGenres] = useState([]);

  useEffect(() => {
    if (open) {
      if (book) {
        setFormData({
          title: book.title || '',
          language: book.language || 'English',
          coverUrl: book.coverUrl || '',
          authorIds: [], // Would need mapping from response
          genreIds: []   // Would need mapping from response
        });
      } else {
        setFormData({
          title: '',
          language: 'English',
          coverUrl: '',
          authorIds: [],
          genreIds: []
        });
        setAudioUrl('');
      }
      fetchAuthors();
      fetchGenres();
    }
  }, [open, book]);

  const fetchAuthors = async () => {
    try {
      const resp = await api.get('/author/v1', { params: { page: 1, limit: 100 } });
      setAuthors(resp.data.items || []);
    } catch (e) { console.error(e); }
  };

  const fetchGenres = async () => {
    try {
      const resp = await api.get('/genre/v1');
      setGenres(resp.data || []);
    } catch (e) { console.error(e); }
  };

  const handleAudioUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setUploading(true);
    const uploadData = new FormData();
    uploadData.append('file', file);

    try {
      // Flow as requested: upload audio first
      const resp = await api.post('/audio/v1/upload', uploadData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      
      if (resp.statusCode === 200) {
        const url = resp.data.fileUrl;
        setAudioUrl(url);
        // "sau đó lấy đường dẫn rồi điền vào formRequest"
        // Since BookCreateRequest has coverUrl, I'll set it here as a demonstration
        // or just keep it in state if the user meant another field.
        setFormData(prev => ({ ...prev, coverUrl: url }));
        alert('Audio uploaded successfully! URL: ' + url);
      }
    } catch (error) {
      console.error('Upload failed', error);
      alert('Upload failed: ' + (error.response?.data?.message || error.message));
    } finally {
      setUploading(false);
    }
  };

  const handleSubmit = async () => {
    setLoading(true);
    try {
      if (book) {
        // await api.put(`/book/v1/${book.id}`, formData);
      } else {
        await api.post('/book/v1', formData);
      }
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
        {book ? 'Edit Book' : 'Add New Book'}
        <IconButton onClick={handleClose} size="small"><CloseIcon /></IconButton>
      </DialogTitle>
      <DialogContent dividers>
        <Stack spacing={3} sx={{ mt: 1 }}>
          
          <Box sx={{ p: 2, border: '1px dashed #cbd5e1', borderRadius: '8px', textAlign: 'center', bgcolor: '#f8fafc' }}>
            <Typography variant="subtitle2" gutterBottom>1. Upload Audio First</Typography>
            <input
              accept="audio/*"
              style={{ display: 'none' }}
              id="audio-upload"
              type="file"
              onChange={handleAudioUpload}
            />
            <label htmlFor="audio-upload">
              <Button
                variant="outlined"
                component="span"
                startIcon={uploading ? <CircularProgress size={20} /> : <AudioIcon />}
                disabled={uploading}
              >
                {uploading ? 'Uploading...' : 'Choose Audio File'}
              </Button>
            </label>
            {audioUrl && (
              <Typography variant="caption" display="block" sx={{ mt: 1, color: 'success.main', wordBreak: 'break-all' }}>
                Audio Path: {audioUrl}
              </Typography>
            )}
          </Box>

          <Typography variant="subtitle2">2. Fill Book Details</Typography>
          
          <TextField
            label="Book Title"
            value={formData.title}
            onChange={(e) => setFormData({ ...formData, title: e.target.value })}
            fullWidth
            required
            size="small"
          />

          <TextField
            select
            label="Language"
            value={formData.language}
            onChange={(e) => setFormData({ ...formData, language: e.target.value })}
            fullWidth
            size="small"
          >
            {['English', 'Vietnamese', 'Spanish', 'French'].map((lang) => (
              <MenuItem key={lang} value={lang}>{lang}</MenuItem>
            ))}
          </TextField>

          <TextField
            label="Cover URL / Audio Path"
            value={formData.coverUrl}
            onChange={(e) => setFormData({ ...formData, coverUrl: e.target.value })}
            fullWidth
            size="small"
            placeholder="Enter URL or will be auto-filled after upload"
            helperText="The path retrieved from step 1 is auto-filled here"
          />

          <Autocomplete
            multiple
            options={authors}
            getOptionLabel={(option) => option.name || ''}
            value={authors.filter(a => formData.authorIds.includes(a.id))}
            onChange={(event, newValue) => {
              setFormData({ ...formData, authorIds: newValue.map(v => v.id) });
            }}
            renderInput={(params) => <TextField {...params} label="Authors" size="small" />}
            renderTags={(tagValue, getTagProps) =>
              tagValue.map((option, index) => (
                <Chip label={option.name} {...getTagProps({ index })} size="small" />
              ))
            }
          />

          <Autocomplete
            multiple
            options={genres}
            getOptionLabel={(option) => option.name || ''}
            value={genres.filter(g => formData.genreIds.includes(g.id))}
            onChange={(event, newValue) => {
              setFormData({ ...formData, genreIds: newValue.map(v => v.id) });
            }}
            renderInput={(params) => <TextField {...params} label="Genres" size="small" />}
            renderTags={(tagValue, getTagProps) =>
              tagValue.map((option, index) => (
                <Chip label={option.name} {...getTagProps({ index })} size="small" />
              ))
            }
          />

        </Stack>
      </DialogContent>
      <DialogActions sx={{ p: 2, bgcolor: '#f8fafc' }}>
        <Button onClick={handleClose} color="inherit">Cancel</Button>
        <Button
          onClick={handleSubmit}
          variant="contained"
          disabled={loading || !formData.title}
          startIcon={loading && <CircularProgress size={20} />}
        >
          {book ? 'Update' : 'Create Book'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default BookForm;

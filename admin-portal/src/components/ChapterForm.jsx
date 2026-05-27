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
  CircularProgress
} from '@mui/material';
import { Close as CloseIcon } from '@mui/icons-material';
import api from '../api/axios';

const ChapterForm = ({ open, handleClose, bookId, chapter, refresh }) => {
  const [formData, setFormData] = useState({
    bookId: parseInt(bookId),
    title: '',
    description: '',
    number: 1
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (open) {
      if (chapter) {
        setFormData({
          bookId: parseInt(bookId),
          title: chapter.title || '',
          description: chapter.description || '',
          number: chapter.number || 1
        });
      } else {
        setFormData({
          bookId: parseInt(bookId),
          title: '',
          description: '',
          number: 1
        });
      }
    }
  }, [open, chapter, bookId]);

  const handleSubmit = async () => {
    setLoading(true);
    try {
      if (chapter) {
        // await api.put(`/chapter/v1/${chapter.id}`, formData);
      } else {
        await api.post('/chapter/v1', formData);
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
    <Dialog open={open} onClose={handleClose} maxWidth="xs" fullWidth>
      <DialogTitle sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        {chapter ? 'Chỉnh sửa Chương' : 'Thêm Chương mới'}
        <IconButton onClick={handleClose} size="small"><CloseIcon /></IconButton>
      </DialogTitle>
      <DialogContent dividers>
        <Stack spacing={3} sx={{ mt: 1 }}>
          <TextField
            label="Số thứ tự Chương"
            type="number"
            value={formData.number}
            onChange={(e) => setFormData({ ...formData, number: parseInt(e.target.value) })}
            fullWidth
            required
            size="small"
          />
          <TextField
            label="Tiêu đề"
            value={formData.title}
            onChange={(e) => setFormData({ ...formData, title: e.target.value })}
            fullWidth
            required
            size="small"
          />
          <TextField
            label="Mô tả"
            value={formData.description}
            onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            fullWidth
            multiline
            rows={3}
            size="small"
          />
        </Stack>
      </DialogContent>
      <DialogActions sx={{ p: 2, bgcolor: '#f8fafc' }}>
        <Button onClick={handleClose} color="inherit">Hủy</Button>
        <Button
          onClick={handleSubmit}
          variant="contained"
          disabled={loading || !formData.title}
          startIcon={loading && <CircularProgress size={20} />}
          sx={{ borderRadius: '10px' }}
        >
          {chapter ? 'Cập nhật' : 'Tạo Chương'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ChapterForm;

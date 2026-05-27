import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Button,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  IconButton,
  Breadcrumbs,
  Link,
  CircularProgress,
  Stack,
  Fade
} from '@mui/material';
import {
  ArrowBack as BackIcon,
  Refresh as RefreshIcon
} from '@mui/icons-material';
import api from '../api/axios';

const SentenceList = () => {
  const { bookId, chapterId, pageId } = useParams();
  const navigate = useNavigate();
  const [sentences, setSentences] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchData = async () => {
    setLoading(true);
    try {
      const response = await api.get('/sentence/v1', {
        params: { pageId }
      });
      if (response.statusCode === 200) {
        setSentences(response.data || []);
      }
    } catch (error) {
      console.error('Failed to fetch sentences', error);
      setSentences([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [pageId]);

  return (
    <Fade in={true}>
      <Box sx={{ p: 1 }}>
        {/* Header Section */}
        <Box sx={{ mb: 3 }}>
          <Breadcrumbs sx={{ mb: 1, fontSize: '0.85rem' }}>
            <Link color="inherit" onClick={() => navigate('/books')} sx={{ cursor: 'pointer', textDecoration: 'none' }}>
              Danh sách sách
            </Link>
            <Link 
              color="inherit" 
              onClick={() => navigate(`/books/${bookId}/chapters`)} 
              sx={{ cursor: 'pointer', textDecoration: 'none' }}
            >
              Quản lý chương
            </Link>
            <Link 
              color="inherit" 
              onClick={() => navigate(`/books/${bookId}/chapters/${chapterId}/pages`)} 
              sx={{ cursor: 'pointer', textDecoration: 'none' }}
            >
              Phân đoạn trang
            </Link>
            <Typography color="text.primary" sx={{ fontSize: '0.85rem' }}>Chi tiết câu</Typography>
          </Breadcrumbs>
          
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Stack direction="row" spacing={2} alignItems="center">
              <IconButton onClick={() => navigate(`/books/${bookId}/chapters/${chapterId}/pages`)} sx={{ backgroundColor: '#f1f5f9' }}>
                <BackIcon />
              </IconButton>
              <Box>
                <Typography variant="h4" sx={{ fontWeight: 700, color: '#1e293b' }}>
                  Chi tiết nội dung Trang
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Danh sách các câu (Sentences) thuộc trang: <b>{pageId}</b>
                </Typography>
              </Box>
            </Stack>
          </Box>
        </Box>

        {/* Info Area */}
        <Paper sx={{ p: 2, mb: 0, borderRadius: '12px 12px 0 0', border: '1px solid #f1f5f9', boxShadow: 'none', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="subtitle1" sx={{ fontWeight: 600, color: '#475569' }}>
            Danh sách các Câu
          </Typography>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
             <IconButton size="small" onClick={fetchData} sx={{ color: '#64748b' }}>
               <RefreshIcon fontSize="small" />
             </IconButton>
             <Typography variant="body2" sx={{ color: '#64748b' }}>
               Tổng số: <b>{sentences.length}</b> câu
             </Typography>
          </Box>
        </Paper>

        {/* Table Section */}
        <TableContainer component={Paper} sx={{ borderRadius: '0 0 12px 12px', border: '1px solid #f1f5f9', borderTop: 'none', boxShadow: 'none' }}>
          <Table>
            <TableHead sx={{ backgroundColor: '#f8fafc' }}>
              <TableRow>
                <TableCell sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem', width: 60 }}>ID</TableCell>
                <TableCell sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem' }}>NỘI DUNG</TableCell>
                <TableCell sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem' }}>PHIÊN ÂM</TableCell>
                <TableCell align="center" sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem', width: 150 }}>THỜI GIAN (Bắt đầu - Kết thúc)</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={4} align="center" sx={{ py: 8 }}>
                    <CircularProgress size={32} thickness={5} />
                  </TableCell>
                </TableRow>
              ) : sentences.length > 0 ? (
                sentences.map((sentence) => (
                  <TableRow key={sentence.id} sx={{ '&:hover': { backgroundColor: '#f8fafc' } }}>
                    <TableCell>
                      <Typography variant="body2" sx={{ fontWeight: 600, color: '#64748b' }}>
                        #{sentence.id}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      <Typography variant="body2" sx={{ fontWeight: 600, color: '#1e293b' }}>
                        {sentence.content}
                      </Typography>
                    </TableCell>
                    <TableCell>
                      <Typography variant="body2" sx={{ color: '#475569', fontStyle: 'italic' }}>
                        {sentence.transcription1 || '-'}
                      </Typography>
                    </TableCell>
                    <TableCell align="center">
                      <Box sx={{ display: 'inline-flex', px: 1.5, py: 0.5, borderRadius: '4px', backgroundColor: 'rgba(59, 130, 246, 0.1)', color: '#3b82f6', fontWeight: 600 }}>
                        {sentence.startTime} — {sentence.endTime}
                      </Box>
                    </TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={4} align="center" sx={{ py: 6, color: '#64748b' }}>
                    Không có câu nào được tìm thấy cho trang này.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </Fade>
  );
};

export default SentenceList;

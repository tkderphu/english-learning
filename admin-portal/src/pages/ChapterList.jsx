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
  Tooltip,
  Fade,
  Pagination,
  TextField,
  InputAdornment
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  ArrowBack as BackIcon,
  Visibility as ViewIcon,
  Search as SearchIcon
} from '@mui/icons-material';
import api from '../api/axios';

const ChapterList = () => {
  const { bookId } = useParams();
  const navigate = useNavigate();
  const [chapters, setChapters] = useState([]);
  const [bookTitle, setBookTitle] = useState('');
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);

  const fetchBookDetail = async () => {
    try {
      const response = await api.get(`/book/v1/${bookId}`);
      if (response.statusCode === 200) {
        setBookTitle(response.data.title);
      }
    } catch (e) { console.error(e); }
  };

  const fetchChapters = async () => {
    setLoading(true);
    try {
      const response = await api.get('/chapter/v1', {
        params: { bookId, page, limit: 10 }
      });
      if (response.statusCode === 200) {
        setChapters(response.data.data || []);
        setTotalPages(response.data.total || 0);
        setTotalItems(response.data.data?.length || 0);
      }
    } catch (error) {
      console.error('Failed to fetch chapters', error);
      setChapters([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBookDetail();
  }, [bookId]);

  useEffect(() => {
    fetchChapters();
  }, [bookId, page]);

  const handleAdd = () => {
    navigate(`/books/${bookId}/chapters/add`);
  };

  const handleEdit = (chapter) => {
    navigate(`/books/${bookId}/chapters/${chapter.id}/edit`);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa chương này?')) {
      // Delete logic
    }
  };

  return (
    <Fade in={true}>
      <Box sx={{ p: 1 }}>
        {/* Header Section */}
        <Box sx={{ mb: 3 }}>
          <Breadcrumbs sx={{ mb: 1, fontSize: '0.85rem' }}>
            <Link color="inherit" onClick={() => navigate('/books')} sx={{ cursor: 'pointer', textDecoration: 'none' }}>
              Danh sách sách
            </Link>
            <Typography color="text.primary" sx={{ fontSize: '0.85rem' }}>Quản lý chương</Typography>
          </Breadcrumbs>
          
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Stack direction="row" spacing={2} alignItems="center">
              <IconButton onClick={() => navigate('/books')} sx={{ backgroundColor: '#f1f5f9' }}>
                <BackIcon />
              </IconButton>
              <Box>
                <Typography variant="h4" sx={{ fontWeight: 700, color: '#1e293b' }}>
                  Quản lý chương
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Nội dung cho sách: <b>{bookTitle}</b>
                </Typography>
              </Box>
            </Stack>
            <Button
              variant="contained"
              startIcon={<AddIcon />}
              onClick={handleAdd}
              sx={{ 
                backgroundColor: '#ff6b00', 
                '&:hover': { backgroundColor: '#e65100' },
                borderRadius: '8px',
                textTransform: 'none',
                fontWeight: 600,
                px: 3
              }}
            >
              Thêm Chương
            </Button>
          </Box>
        </Box>

        {/* Info Area */}
        <Paper sx={{ p: 2, mb: 0, borderRadius: '12px 12px 0 0', border: '1px solid #f1f5f9', boxShadow: 'none', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="subtitle1" sx={{ fontWeight: 600, color: '#475569' }}>
            Danh sách các chương
          </Typography>
          <Typography variant="body2" sx={{ color: '#64748b' }}>
             Hiển thị trên trang này: <b>{chapters.length}</b> chương
          </Typography>
        </Paper>

        {/* Table Section */}
        <TableContainer component={Paper} sx={{ borderRadius: '0 0 12px 12px', border: '1px solid #f1f5f9', borderTop: 'none', boxShadow: 'none' }}>
          <Table>
            <TableHead sx={{ backgroundColor: '#f8fafc' }}>
              <TableRow>
                <TableCell sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem', width: 80 }}>STT</TableCell>
                <TableCell sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem' }}>TIÊU ĐỀ</TableCell>
                <TableCell sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem' }}>MÔ TẢ</TableCell>
                <TableCell align="center" sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem' }}>SỐ TRANG</TableCell>
                <TableCell align="center" sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem' }}>THỜI LƯỢNG (PHÚT)</TableCell>
                <TableCell align="right" sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem' }}>HÀNH ĐỘNG</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={6} align="center" sx={{ py: 8 }}>
                    <CircularProgress size={32} thickness={5} />
                  </TableCell>
                </TableRow>
              ) : chapters.length > 0 ? (
                chapters.map((chapter) => (
                  <TableRow key={chapter.id} sx={{ '&:hover': { backgroundColor: '#f8fafc' } }}>
                    <TableCell>
                       <Box sx={{ 
                         width: 32, height: 32, borderRadius: '6px', 
                         backgroundColor: '#f1f5f9', display: 'flex', 
                         alignItems: 'center', justifyContent: 'center',
                         fontWeight: 700, color: '#475569', fontSize: '0.85rem'
                       }}>
                         {chapter.number}
                       </Box>
                    </TableCell>
                    <TableCell sx={{ fontWeight: 600, color: '#1e293b' }}>{chapter.title}</TableCell>
                    <TableCell sx={{ color: '#64748b', maxWidth: 300 }}>
                      <Typography variant="body2" noWrap title={chapter.description}>
                        {chapter.description || '-'}
                      </Typography>
                    </TableCell>
                    <TableCell align="center">
                      <Box sx={{ px: 1.5, py: 0.5, borderRadius: '4px', backgroundColor: 'rgba(59, 130, 246, 0.1)', color: '#3b82f6', fontWeight: 600, display: 'inline-block' }}>
                        {chapter.totalPages || 0}
                      </Box>
                    </TableCell>
                    <TableCell align="center">
                       <Typography variant="body2" sx={{ color: '#64748b', fontWeight: 500 }}>
                         {((chapter.totalDuration || 0) / 60000).toFixed(1)}m
                       </Typography>
                    </TableCell>
                    <TableCell align="right">
                      <Stack direction="row" spacing={0.5} justifyContent="flex-end">
                        <Tooltip title="Xem trang">
                          <IconButton 
                            size="small" 
                            onClick={() => navigate(`/books/${bookId}/chapters/${chapter.id}/pages`)}
                            sx={{ color: '#3b82f6' }}
                          >
                            <ViewIcon fontSize="small" />
                          </IconButton>
                        </Tooltip>
                        <IconButton 
                          size="small" 
                          onClick={() => handleEdit(chapter)}
                          sx={{ color: '#3b82f6' }}
                        >
                          <EditIcon fontSize="small" />
                        </IconButton>
                        <IconButton 
                          size="small" 
                          sx={{ color: '#ef4444' }}
                          onClick={() => handleDelete(chapter.id)}
                        >
                          <DeleteIcon fontSize="small" />
                        </IconButton>
                      </Stack>
                    </TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={6} align="center" sx={{ py: 4 }}>
                    Không tìm thấy chương nào.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>

        {/* Pagination Section */}
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
          <Pagination
            count={totalPages}
            page={page}
            onChange={(e, v) => setPage(v)}
            shape="rounded"
            color="primary"
          />
        </Box>
      </Box>
    </Fade>
  );
};

export default ChapterList;

import React, { useState, useEffect } from 'react';
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
  TextField,
  Pagination,
  CircularProgress,
  Stack,
  InputAdornment,
  Tooltip,
  Fade,
  Breadcrumbs,
  Link,
  MenuItem,
  Select,
  FormControl
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Search as SearchIcon,
  Visibility as ViewIcon,
  KeyboardArrowDown as ArrowDownIcon
} from '@mui/icons-material';
import api from '../api/axios';
import { useNavigate } from 'react-router-dom';

const BookList = () => {
  const navigate = useNavigate();
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  const [keyword, setKeyword] = useState('');

  const fetchBooks = async () => {
    setLoading(true);
    try {
      const response = await api.get('/book/v1', {
        params: { page, limit: 10, keyword }
      });
      if (response.statusCode === 200) {
        // data.data is the list if using PageResponse, or data if using simple List
        const list = Array.isArray(response.data.data) ? response.data.data : (Array.isArray(response.data) ? response.data : []);
        setBooks(list);
        
        // As per Util.java, response.data.total maps to page.getTotalPages()
        setTotalPages(response.data.total || 1);
        setTotalItems(list.length); // We don't have total items count in PageResponse
      }
    } catch (error) {
      console.error('Failed to fetch books', error);
      setBooks([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBooks();
  }, [page]);

  const handleSearch = (e) => {
    if (e.key === 'Enter') {
      setPage(1);
      fetchBooks();
    }
  };

  return (
    <Fade in={true}>
      <Box sx={{ p: 1 }}>
        {/* Header Section */}
        <Box sx={{ mb: 3 }}>
          <Breadcrumbs sx={{ mb: 1, fontSize: '0.85rem' }}>
            <Link color="inherit" onClick={() => navigate('/')} sx={{ cursor: 'pointer', textDecoration: 'none' }}>
              Trang chủ
            </Link>
            <Typography color="text.primary" sx={{ fontSize: '0.85rem' }}>Danh sách sách</Typography>
          </Breadcrumbs>
          
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Typography variant="h4" sx={{ fontWeight: 700, color: '#1e293b' }}>
              Danh sách sách
            </Typography>
            <Button
              variant="contained"
              startIcon={<AddIcon />}
              onClick={() => navigate('/books/add')}
              sx={{ 
                backgroundColor: '#ff6b00', 
                '&:hover': { backgroundColor: '#e65100' },
                borderRadius: '8px',
                textTransform: 'none',
                fontWeight: 600,
                px: 3,
                py: 1
              }}
            >
              Thêm Sách
            </Button>
          </Box>
        </Box>

        {/* Filter Section */}
        <Paper sx={{ p: 2, mb: 0, borderRadius: '12px 12px 0 0', border: '1px solid #f1f5f9', boxShadow: 'none' }}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: 2 }}>
            <Stack direction="row" spacing={2} sx={{ flexGrow: 1 }}>
              <TextField
                placeholder="Tìm kiếm tiêu đề, tác giả..."
                size="small"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                onKeyPress={handleSearch}
                sx={{ width: 400 }}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <SearchIcon sx={{ color: '#94a3b8', fontSize: 20 }} />
                    </InputAdornment>
                  ),
                  sx: { borderRadius: '8px' }
                }}
              />
            </Stack>

            <Typography variant="body2" sx={{ color: '#64748b' }}>
              Tổng cộng: <b>{totalItems}</b> bản ghi
            </Typography>
          </Box>
        </Paper>

        {/* Table Section */}
        <TableContainer component={Paper} sx={{ borderRadius: '0 0 12px 12px', border: '1px solid #f1f5f9', borderTop: 'none', boxShadow: 'none' }}>
          <Table>
            <TableHead sx={{ backgroundColor: '#f8fafc' }}>
              <TableRow>
                <TableCell sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem', py: 1.5 }}>TIÊU ĐỀ SÁCH</TableCell>
                <TableCell sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem', py: 1.5 }}>TÁC GIẢ</TableCell>
                <TableCell sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem', py: 1.5 }}>NGÔN NGỮ</TableCell>
                <TableCell sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem', py: 1.5 }}>THỂ LOẠI</TableCell>
                <TableCell align="right" sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem', py: 1.5 }}>HÀNH ĐỘNG</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={5} align="center" sx={{ py: 8 }}>
                    <CircularProgress size={32} thickness={5} />
                  </TableCell>
                </TableRow>
              ) : books.length > 0 ? (
                books.map((book) => (
                  <TableRow key={book.id} sx={{ '&:hover': { backgroundColor: '#f8fafc' } }}>
                    <TableCell sx={{ fontWeight: 500, color: '#1e293b' }}>{book.title}</TableCell>
                    <TableCell sx={{ color: '#64748b' }}>{book.authors || '-'}</TableCell>
                    <TableCell sx={{ color: '#64748b' }}>{book.language}</TableCell>
                    <TableCell sx={{ color: '#64748b' }}>{book.genresName || '-'}</TableCell>
                    <TableCell align="right">
                      <Stack direction="row" spacing={0.5} justifyContent="flex-end">
                        <IconButton 
                          size="small" 
                          onClick={() => navigate(`/books/${book.id}/chapters`)}
                          sx={{ color: '#3b82f6' }}
                        >
                          <ViewIcon fontSize="small" />
                        </IconButton>
                        <IconButton 
                          size="small" 
                          onClick={() => navigate(`/books/${book.id}/edit`)}
                          sx={{ color: '#3b82f6' }}
                        >
                          <EditIcon fontSize="small" />
                        </IconButton>
                        <IconButton 
                          size="small" 
                          sx={{ color: '#ef4444' }}
                        >
                          <DeleteIcon fontSize="small" />
                        </IconButton>
                      </Stack>
                    </TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={5} align="center" sx={{ py: 4 }}>
                    Không tìm thấy sách nào.
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
            sx={{
                '& .Mui-selected': { backgroundColor: '#3b82f6 !important' }
            }}
          />
        </Box>
      </Box>
    </Fade>
  );
};

export default BookList;

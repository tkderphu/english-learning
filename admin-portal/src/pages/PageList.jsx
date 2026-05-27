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
  Dialog,
  DialogTitle,
  DialogContent
} from '@mui/material';
import {
  Add as AddIcon,
  Delete as DeleteIcon,
  ArrowBack as BackIcon,
  Refresh as RefreshIcon,
  PlayCircleOutline as PlayIcon,
  Visibility as ViewIcon,
  PauseCircleOutline as PauseIcon,
  Close as CloseIcon
} from '@mui/icons-material';
import api from '../api/axios';

const PageList = () => {
  const { bookId, chapterId } = useParams();
  const navigate = useNavigate();
  const [pages, setPages] = useState([]);
  const [chapterTitle, setChapterTitle] = useState('');
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [playingId, setPlayingId] = useState(null);
  const [audioRef, setAudioRef] = useState(null);
  const [openDetailDialog, setOpenDetailDialog] = useState(false);
  const [detailSentences, setDetailSentences] = useState([]);
  const [detailPageNumber, setDetailPageNumber] = useState(null);
  const [loadingDetail, setLoadingDetail] = useState(false);

  const handleOpenDetail = async (pageId, pageNumber) => {
    setDetailPageNumber(pageNumber);
    setOpenDetailDialog(true);
    setLoadingDetail(true);
    try {
      const response = await api.get('/sentence/v1', { params: { pageId } });
      if (response.statusCode === 200) {
        setDetailSentences(response.data || []);
      }
    } catch (e) {
      console.error(e);
      setDetailSentences([]);
    } finally {
      setLoadingDetail(false);
    }
  };

  const handlePlayAudio = (pageId, url) => {
    if (playingId === pageId) {
      audioRef.pause();
      setPlayingId(null);
    } else {
      if (audioRef) audioRef.pause();
      const audio = new Audio(url);
      audio.onended = () => setPlayingId(null);
      audio.play();
      setAudioRef(audio);
      setPlayingId(pageId);
    }
  };

  const fetchChapterDetail = async () => {
    try {
      const response = await api.get('/chapter/v1', { params: { bookId, page: 1, limit: 100 } });
      if (response.statusCode === 200) {
        const chapter = response.data.data?.find(c => c.id === parseInt(chapterId));
        if (chapter) setChapterTitle(chapter.title);
      }
    } catch (e) { console.error(e); }
  };

  const fetchData = async () => {
    setLoading(true);
    try {
      const response = await api.get('/page/v1', {
        params: { chapterId, page, limit: 10 }
      });
      if (response.statusCode === 200) {
        setPages(response.data.content || []);
        setTotalPages(response.data.totalPages || 0);
      }
    } catch (error) {
      console.error('Failed to fetch pages', error);
      setPages([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchChapterDetail();
  }, [bookId, chapterId]);

  useEffect(() => {
    fetchData();
  }, [chapterId, page]);

  const handleAdd = () => {
    navigate(`/books/${bookId}/chapters/${chapterId}/pages/add`);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa trang này?')) {
      // Logic for deleting page
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
            <Link 
              color="inherit" 
              onClick={() => navigate(`/books/${bookId}/chapters`)} 
              sx={{ cursor: 'pointer', textDecoration: 'none' }}
            >
              Quản lý chương
            </Link>
            <Typography color="text.primary" sx={{ fontSize: '0.85rem' }}>Phân đoạn trang</Typography>
          </Breadcrumbs>
          
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Stack direction="row" spacing={2} alignItems="center">
              <IconButton onClick={() => navigate(`/books/${bookId}/chapters`)} sx={{ backgroundColor: '#f1f5f9' }}>
                <BackIcon />
              </IconButton>
              <Box>
                <Typography variant="h4" sx={{ fontWeight: 700, color: '#1e293b' }}>
                  Phân đoạn trang
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Chi tiết nội dung cho chương: <b>{chapterTitle || chapterId}</b>
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
              Thêm Trang
            </Button>
          </Box>
        </Box>

        {/* Info Area */}
        <Paper sx={{ p: 2, mb: 0, borderRadius: '12px 12px 0 0', border: '1px solid #f1f5f9', boxShadow: 'none', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <Typography variant="subtitle1" sx={{ fontWeight: 600, color: '#475569' }}>
            Danh sách các trang
          </Typography>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
             <IconButton size="small" onClick={fetchData} sx={{ color: '#64748b' }}>
               <RefreshIcon fontSize="small" />
             </IconButton>
             <Typography variant="body2" sx={{ color: '#64748b' }}>
               Tổng số: <b>{pages.length}</b> trang
             </Typography>
          </Box>
        </Paper>

        {/* Table Section */}
        <TableContainer component={Paper} sx={{ borderRadius: '0 0 12px 12px', border: '1px solid #f1f5f9', borderTop: 'none', boxShadow: 'none' }}>
          <Table>
            <TableHead sx={{ backgroundColor: '#f8fafc' }}>
              <TableRow>
                <TableCell sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem', width: '33.33%' }}>SỐ TRANG</TableCell>
                <TableCell align="center" sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem', width: '33.33%' }}>ÂM THANH</TableCell>
                <TableCell align="right" sx={{ color: '#475569', fontWeight: 600, fontSize: '0.75rem', width: '33.33%' }}>HÀNH ĐỘNG</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {loading ? (
                <TableRow>
                  <TableCell colSpan={3} align="center" sx={{ py: 8 }}>
                    <CircularProgress size={32} thickness={5} />
                  </TableCell>
                </TableRow>
              ) : pages.length > 0 ? (
                pages.map((pageObj) => {
                  return (
                    <TableRow key={pageObj.id} sx={{ '&:hover': { backgroundColor: '#f8fafc' } }}>
                      <TableCell>
                        <Box sx={{ 
                          width: 40, height: 40, borderRadius: '10px', 
                          display: 'flex', alignItems: 'center', justifyContent: 'center',
                          backgroundColor: 'rgba(14, 165, 233, 0.1)', color: '#0ea5e9', fontWeight: 800
                        }}>
                          {pageObj.number}
                        </Box>
                      </TableCell>
                      <TableCell align="center">
                        {pageObj.audio?.fileUrl ? (
                          <IconButton 
                            size="small" 
                            onClick={() => handlePlayAudio(pageObj.id, pageObj.audio.fileUrl)}
                            color={playingId === pageObj.id ? "secondary" : "primary"}
                            sx={{ backgroundColor: playingId === pageObj.id ? 'rgba(156, 39, 176, 0.1)' : 'rgba(59, 130, 246, 0.1)' }}
                          >
                            {playingId === pageObj.id ? <PauseIcon /> : <PlayIcon />}
                          </IconButton>
                        ) : (
                          <Typography variant="caption" color="text.disabled">Không có</Typography>
                        )}
                      </TableCell>
                      <TableCell align="right">
                        <Stack direction="row" spacing={0.5} justifyContent="flex-end">
                          <Tooltip title="Xem chi tiết">
                            <IconButton 
                              size="small" 
                              onClick={() => handleOpenDetail(pageObj.id, pageObj.number)}
                              sx={{ color: '#3b82f6' }}
                            >
                              <ViewIcon fontSize="small" />
                            </IconButton>
                          </Tooltip>
                          <Tooltip title="Xóa trang">
                            <IconButton 
                              size="small" 
                              color="error" 
                              onClick={() => handleDelete(pageObj.id)}
                              sx={{ backgroundColor: 'rgba(239, 68, 68, 0.1)', '&:hover': { backgroundColor: 'rgba(239, 68, 68, 0.2)' } }}
                            >
                              <DeleteIcon fontSize="small" />
                            </IconButton>
                          </Tooltip>
                        </Stack>
                      </TableCell>
                    </TableRow>
                  );
                })
              ) : (
                <TableRow>
                  <TableCell colSpan={3} align="center" sx={{ py: 4 }}>
                    Không tìm thấy trang nào trong chương này.
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

        {/* Detail Dialog */}
        <Dialog 
          open={openDetailDialog} 
          onClose={() => setOpenDetailDialog(false)}
          maxWidth="md"
          fullWidth
          PaperProps={{ sx: { borderRadius: '12px' } }}
        >
          <DialogTitle sx={{ backgroundColor: '#f8fafc', borderBottom: '1px solid #e2e8f0', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <Typography variant="h6" sx={{ fontWeight: 700, color: '#1e293b' }}>
              Nội dung Trang {detailPageNumber}
            </Typography>
            <IconButton onClick={() => setOpenDetailDialog(false)}>
              <CloseIcon />
            </IconButton>
          </DialogTitle>
          <DialogContent sx={{ p: 4, minHeight: 200 }}>
            {loadingDetail ? (
              <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
                <CircularProgress />
              </Box>
            ) : detailSentences.length > 0 ? (
              <Typography variant="body1" sx={{ color: '#334155', lineHeight: 1.8, fontSize: '1.05rem', whiteSpace: 'pre-wrap' }}>
                {detailSentences.map(s => s.content).join(' ')}
              </Typography>
            ) : (
              <Typography variant="body1" sx={{ color: '#94a3b8', fontStyle: 'italic', textAlign: 'center' }}>
                Trang này không có nội dung văn bản.
              </Typography>
            )}
          </DialogContent>
        </Dialog>
      </Box>
    </Fade>
  );
};

export default PageList;

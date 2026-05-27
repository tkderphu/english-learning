import { useState } from 'react'
import { Routes, Route, useNavigate, useLocation } from 'react-router-dom'
import { 
  Box, 
  Drawer, 
  AppBar, 
  Toolbar, 
  List, 
  Typography, 
  Divider, 
  IconButton, 
  ListItem, 
  ListItemButton, 
  ListItemIcon, 
  ListItemText,
  Container
} from '@mui/material'
import {
  Menu as MenuIcon,
  ChevronLeft as ChevronLeftIcon,
  Dashboard as DashboardIcon,
  MenuBook as BookIcon,
  People as PeopleIcon,
  Category as CategoryIcon,
  MusicNote as AudioIcon
} from '@mui/icons-material'

import BookList from './pages/BookList'
import Dashboard from './pages/Dashboard'
import ChapterList from './pages/ChapterList'
import PageList from './pages/PageList'
import SentenceList from './pages/SentenceList'
import BookFormPage from './pages/BookFormPage'
import ChapterFormPage from './pages/ChapterFormPage'
import PageFormPage from './pages/PageFormPage'

const drawerWidth = 240;

function App() {
  const [open, setOpen] = useState(true);
  const navigate = useNavigate();
  const location = useLocation();

  const Sidebar = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const menuItems = [
      { text: 'Bảng điều khiển', icon: <DashboardIcon />, path: '/' },
      { text: 'Quản lý sách', icon: <BookIcon />, path: '/books' },
      { text: 'Tác giả', icon: <PeopleIcon />, path: '/authors' },
      { text: 'Thể loại', icon: <CategoryIcon />, path: '/genres' },
    ];

    return (
      <Box sx={{ p: 2, height: '100%' }}>
        <Box sx={{ mb: 4, px: 2, py: 1 }}>
          <Typography variant="h6" sx={{ fontWeight: 900, color: 'primary.main', letterSpacing: -1 }}>
            ESL ADMIN
          </Typography>
        </Box>
        <List sx={{ px: 0 }}>
          {menuItems.map((item) => (
            <ListItem key={item.text} disablePadding sx={{ mb: 1 }}>
              <ListItemButton
                onClick={() => navigate(item.path)}
                sx={{
                  borderRadius: '12px',
                  py: 1.5,
                  backgroundColor: location.pathname === item.path ? 'rgba(99, 102, 241, 0.08)' : 'transparent',
                  color: location.pathname === item.path ? 'primary.main' : 'text.secondary',
                  '&:hover': {
                    backgroundColor: 'rgba(99, 102, 241, 0.04)',
                    color: 'primary.main',
                  },
                }}
              >
                <ListItemIcon sx={{ color: 'inherit', minWidth: 40 }}>
                  {item.icon}
                </ListItemIcon>
                <ListItemText 
                  primary={item.text} 
                  primaryTypographyProps={{ fontWeight: location.pathname === item.path ? 700 : 500, fontSize: '0.9rem' }} 
                />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
        
        <Box sx={{ mt: 'auto', p: 2, backgroundColor: 'rgba(99, 102, 241, 0.05)', borderRadius: '16px' }}>
          <Typography variant="caption" color="text.secondary" sx={{ fontWeight: 600 }}>v2.0 Phiên bản Cao cấp</Typography>
        </Box>
      </Box>
    );
  };

  const toggleDrawer = () => {
    setOpen(!open);
  };

  return (
    <Box sx={{ display: 'flex' }}>
      <AppBar position="absolute" sx={{ 
        zIndex: (theme) => theme.zIndex.drawer + 1,
        backgroundColor: '#ffffff',
        color: '#1e293b',
        boxShadow: 'none',
        borderBottom: '1px solid #e2e8f0'
      }}>
        <Toolbar sx={{ pr: '24px' }}>
          <IconButton
            edge="start"
            color="inherit"
            aria-label="open drawer"
            onClick={toggleDrawer}
            sx={{ marginRight: '36px', ...(open && { display: 'none' }) }}
          >
            <MenuIcon />
          </IconButton>
          <Typography component="h1" variant="h6" color="primary" noWrap sx={{ flexGrow: 1, fontWeight: 700 }}>
            Hệ thống Quản trị ESL
          </Typography>
        </Toolbar>
      </AppBar>
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: { 
            width: drawerWidth, 
            boxSizing: 'border-box',
            borderRight: '1px solid rgba(0,0,0,0.05)',
            backgroundColor: '#ffffff'
          },
        }}
      >
        <Sidebar />
      </Drawer>
      <Box
        component="main"
        sx={{
          backgroundColor: (theme) => theme.palette.grey[50],
          flexGrow: 1,
          height: '100vh',
          overflow: 'auto',
          pt: 8
        }}
      >
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/books" element={<BookList />} />
            <Route path="/books/add" element={<BookFormPage />} />
            <Route path="/books/:id/edit" element={<BookFormPage />} />
            <Route path="/books/:bookId/chapters" element={<ChapterList />} />
            <Route path="/books/:bookId/chapters/add" element={<ChapterFormPage />} />
            <Route path="/books/:bookId/chapters/:chapterId/edit" element={<ChapterFormPage />} />
            <Route path="/books/:bookId/chapters/:chapterId/pages" element={<PageList />} />
            <Route path="/books/:bookId/chapters/:chapterId/pages/add" element={<PageFormPage />} />
            <Route path="/books/:bookId/chapters/:chapterId/pages/:pageId/edit" element={<PageFormPage />} />
            <Route path="/authors" element={<Typography variant="h4">Quản lý Tác giả</Typography>} />
            <Route path="/genres" element={<Typography variant="h4">Quản lý Thể loại</Typography>} />
          </Routes>
        </Container>
      </Box>
    </Box>
  )
}

export default App

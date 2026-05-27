import React from 'react';
import { Box, Grid, Paper, Typography, Card, CardContent, Stack, Fade, Divider, IconButton } from '@mui/material';
import { 
  MenuBook as BookIcon, 
  People as PeopleIcon, 
  Category as CategoryIcon, 
  TrendingUp as TrendingUpIcon,
  MoreVert as MoreIcon
} from '@mui/icons-material';

const Dashboard = () => {
  const stats = [
    { title: 'Tổng số Sách', value: '128', icon: <BookIcon />, gradient: 'linear-gradient(135deg, #6366f1 0%, #4f46e5 100%)', color: '#ffffff' },
    { title: 'Tác giả', value: '45', icon: <PeopleIcon />, gradient: 'linear-gradient(135deg, #ec4899 0%, #d946ef 100%)', color: '#ffffff' },
    { title: 'Thể loại', value: '12', icon: <CategoryIcon />, gradient: 'linear-gradient(135deg, #10b981 0%, #059669 100%)', color: '#ffffff' },
    { title: 'Giờ học', value: '1.2k', icon: <TrendingUpIcon />, gradient: 'linear-gradient(135deg, #f59e0b 0%, #d97706 100%)', color: '#ffffff' },
  ];

  return (
    <Fade in={true}>
      <Box sx={{ pb: 4 }}>
        <Box sx={{ mb: 5 }}>
          <Typography variant="h3" sx={{ fontWeight: 800, color: '#1e293b', letterSpacing: -1 }}>
            Chào mừng quay lại, Quản trị viên
          </Typography>
          <Typography variant="body1" color="text.secondary">Dưới đây là tổng quan về thư viện nội dung ESL của bạn hôm nay.</Typography>
        </Box>
        
        <Grid container spacing={4}>
          {stats.map((stat) => (
            <Grid item xs={12} sm={6} md={3} key={stat.title}>
              <Card sx={{ 
                borderRadius: '24px', 
                border: 'none', 
                background: stat.gradient,
                boxShadow: '0 10px 25px -5px rgba(0,0,0,0.1)',
                color: 'white',
                position: 'relative',
                overflow: 'hidden'
              }}>
                <Box sx={{ 
                    position: 'absolute', 
                    top: -20, 
                    right: -20, 
                    width: 100, 
                    height: 100, 
                    borderRadius: '50%', 
                    backgroundColor: 'rgba(255,255,255,0.1)' 
                }} />
                <CardContent sx={{ p: 4 }}>
                  <Stack spacing={2}>
                    <Box sx={{ 
                        p: 1.5, 
                        borderRadius: '16px', 
                        backgroundColor: 'rgba(255,255,255,0.2)', 
                        display: 'flex',
                        width: 'fit-content'
                    }}>
                      {React.cloneElement(stat.icon, { sx: { fontSize: 24 } })}
                    </Box>
                    <Box>
                      <Typography variant="h3" sx={{ fontWeight: 800, lineHeight: 1 }}>{stat.value}</Typography>
                      <Typography variant="body2" sx={{ opacity: 0.8, fontWeight: 600, mt: 0.5 }}>{stat.title}</Typography>
                    </Box>
                  </Stack>
                </CardContent>
              </Card>
            </Grid>
          ))}
          
          <Grid item xs={12} md={8}>
            <Paper sx={{ p: 4, borderRadius: '32px', height: 400 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                    <Typography variant="h6" sx={{ fontWeight: 800 }}>Tăng trưởng Thư viện</Typography>
                    <IconButton size="small"><MoreIcon /></IconButton>
                </Box>
                <Divider sx={{ mb: 4 }} />
                <Box sx={{ height: '70%', display: 'flex', alignItems: 'center', justifyContent: 'center', backgroundColor: '#f8fafc', borderRadius: '24px' }}>
                    <Typography color="text.secondary" variant="body2">Biểu đồ tăng trưởng (Đang phát triển)</Typography>
                </Box>
            </Paper>
          </Grid>
          <Grid item xs={12} md={4}>
            <Paper sx={{ p: 4, borderRadius: '32px', height: 400 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                    <Typography variant="h6" sx={{ fontWeight: 800 }}>Thao tác nhanh</Typography>
                </Box>
                <Divider sx={{ mb: 4 }} />
                <Stack spacing={2}>
                    {[1, 2, 3].map((i) => (
                        <Box key={i} sx={{ 
                            p: 2, 
                            borderRadius: '16px', 
                            backgroundColor: '#f8fafc', 
                            display: 'flex', 
                            alignItems: 'center',
                            cursor: 'pointer',
                            '&:hover': { backgroundColor: 'rgba(99, 102, 241, 0.05)' }
                        }}>
                            <Box sx={{ width: 40, height: 40, borderRadius: '10px', backgroundColor: 'primary.main', mr: 2 }} />
                            <Box>
                                <Typography variant="subtitle2" sx={{ fontWeight: 700 }}>Nhiệm vụ #{1034 + i}</Typography>
                                <Typography variant="caption" color="text.secondary">Yêu cầu xác minh</Typography>
                            </Box>
                        </Box>
                    ))}
                </Stack>
            </Paper>
          </Grid>
        </Grid>
      </Box>
    </Fade>
  );
};

export default Dashboard;

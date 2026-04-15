-- Run once on databases that still have activity_type = 'LESSON' (old book-reading logs).
UPDATE learning_activities SET activity_type = 'BOOK' WHERE activity_type = 'LESSON';

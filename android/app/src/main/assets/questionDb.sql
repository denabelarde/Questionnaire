

--
-- Table structure for table `user`
--
CREATE TABLE IF NOT EXISTS `users` (
	`_id` INTEGER PRIMARY KEY AUTOINCREMENT,
	`object_id` text NOT NULL,
	`username` text NOT NULL,
	`datecreated` text NOT NULL
);


CREATE TABLE IF NOT EXISTS `questions` (
	`_id` INTEGER PRIMARY KEY AUTOINCREMENT,
	`object_id` text NOT NULL,
	`owner_id` text NOT NULL,
	`owner_username` text NOT NULL,
	`title` text NOT NULL,
	`description` text NOT NULL,
	`created_at` text NOT NULL,
	`updated_at` text NOT NULL,
	`answers_count` text NOT NULL
);

CREATE TABLE IF NOT EXISTS `answers` (
	`_id` INTEGER PRIMARY KEY AUTOINCREMENT,
	`object_id` text NOT NULL,
	`answer_string` text NOT NULL,
	`user_id` text NOT NULL,
	`question_id` text NOT NULL
);

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 27, 2025 at 05:33 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `java-give-my-vara`
--

-- --------------------------------------------------------

--
-- Table structure for table `booking`
--

CREATE TABLE `booking` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '1=active,0=deactive',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`id`, `user_id`, `room_id`, `status`, `created_at`) VALUES
(1, 2, 1, 1, '2025-12-24 19:41:31'),
(2, 2, 2, 1, '2025-12-24 19:41:34'),
(3, 3, 5, 0, '2025-12-25 17:47:11'),
(4, 3, 4, 1, '2025-12-25 17:47:34');

-- --------------------------------------------------------

--
-- Table structure for table `buildings`
--

CREATE TABLE `buildings` (
  `id` int(11) NOT NULL,
  `building_name` text NOT NULL,
  `address` text NOT NULL,
  `payment_details` text NOT NULL,
  `user_id` int(11) NOT NULL,
  `policy` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `buildings`
--

INSERT INTO `buildings` (`id`, `building_name`, `address`, `payment_details`, `user_id`, `policy`, `created_at`) VALUES
(1, '1', '1', '1', 1, '1', '2025-12-24 19:39:26'),
(2, '2', '2', '2', 1, '2adfasdf', '2025-12-24 19:39:33'),
(4, 'test', 'test', 'bkash ', 1, 'tk den vai 2', '2025-12-25 17:45:32'),
(5, 'mahi1 building', 'mahi1 building', 'mahi1 building', 4, 'mahi1 building', '2025-12-25 18:04:23'),
(6, 'mahi1 building 2', 'mahi1 building 2', 'mahi1 building 2', 4, 'mahi1 building 2', '2025-12-25 18:04:49'),
(7, 'mahi1 building 3', 'mahi1 building 3', 'mahi1 building 3', 4, 'mahi1 building 3', '2025-12-25 18:04:57');

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `id` int(11) NOT NULL,
  `room_name` varchar(255) NOT NULL,
  `room_details` text NOT NULL,
  `rent` varchar(255) NOT NULL,
  `building_id` int(11) NOT NULL,
  `is_booked` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`id`, `room_name`, `room_details`, `rent`, `building_id`, `is_booked`, `created_at`) VALUES
(1, '20', '20', '20.0', 1, 1, '2025-12-24 19:40:47'),
(2, '2', '20', '20.0', 2, 1, '2025-12-24 19:40:59'),
(4, 'test', 'onek 2', '500.0', 4, 1, '2025-12-25 17:46:12'),
(9, 'room 1', 'room 1', '500.0', 5, 0, '2025-12-25 18:05:17');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` text NOT NULL,
  `user_type` enum('0','1','2','') NOT NULL DEFAULT '2' COMMENT '0-admin,1-owner,2-customer',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `first_name`, `last_name`, `email`, `username`, `password`, `user_type`, `created_at`) VALUES
(1, 'Jawad Hossain', 'Mahi', 'mahi@gmail.com', 'mahi', '$2a$10$CYIUhl.Qhoc/qkHUa1PRk.BuyPWSjDeRlpNAXOFrQxLgPJ0sKubY6', '1', '2025-12-24 19:38:05'),
(2, 'Jawad Customer', 'mahi', 'jawadhossainmahi1@gmail.com', 'mahic', '$2a$10$h6pTLqtOrxVQPtZfcSS17emIoOKMrQ39GN2pgddMu3c2KbwdytkKW', '2', '2025-12-24 19:38:41'),
(3, 'fardin', 'fardin', 'shahriafaysal1@gmail.com', 'fardin', '$2a$10$SgSUy0X8bZjo0fLcE4OLtehNMoM6kJlMBTkJHLx1nJ3LkxiTimANq', '2', '2025-12-25 17:44:27'),
(4, 'mahi1', 'mahi1', 'mahi1@gmail.com', 'mahi1', '$2a$10$SDLcpFAgEIaM4ux4Sk7RvuxCwf568PUNeDqtlsA30nBCF3MZ7BT.q', '1', '2025-12-25 18:03:41');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `buildings`
--
ALTER TABLE `buildings`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `buildings`
--
ALTER TABLE `buildings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `rooms`
--
ALTER TABLE `rooms`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

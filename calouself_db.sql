-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 08, 2024 at 12:04 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `calouself_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
                         `id` varchar(7) NOT NULL,
                         `seller_id` varchar(7) NOT NULL,
                         `name` varchar(100) NOT NULL,
                         `size` varchar(10) NOT NULL,
                         `price` double NOT NULL,
                         `category` varchar(50) NOT NULL,
                         `status` enum('PENDING','APPROVED','DECLINED','SOLD_OUT','INVALID') NOT NULL,
                         `note` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`id`, `seller_id`, `name`, `size`, `price`, `category`, `status`, `note`) VALUES
    ('IID0001', 'UID0001', 'T-shirt', 'S', 200000, 'Shirt', 'DECLINED', 'It looks so wrong.');

-- --------------------------------------------------------

--
-- Table structure for table `offers`
--

CREATE TABLE `offers` (
                          `id` varchar(7) NOT NULL,
                          `user_id` varchar(7) NOT NULL,
                          `item_id` varchar(7) NOT NULL,
                          `price` double NOT NULL,
                          `date` date NOT NULL,
                          `status` enum('PENDING','ACCEPTED','DECLINED') NOT NULL,
                          `reason` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
                                `id` varchar(7) NOT NULL,
                                `user_id` varchar(7) NOT NULL,
                                `item_id` varchar(7) NOT NULL,
                                `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
                         `id` varchar(7) NOT NULL,
                         `username` varchar(50) NOT NULL,
                         `password` varchar(50) NOT NULL,
                         `phone_number` varchar(20) NOT NULL,
                         `address` varchar(100) NOT NULL,
                         `role` enum('SELLER','BUYER') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `phone_number`, `address`, `role`) VALUES
                                                                                          ('UID0001', 'Budi', 'BudiBudi@', '+620888888888', 'Jl. Budi Budi Budi Budi', 'SELLER'),
                                                                                          ('UID0002', 'BudiBudi', 'BudiBudi@', '+628888888888', 'Jl. Budi Budi Budi', 'BUYER');

-- --------------------------------------------------------

--
-- Table structure for table `wishlists`
--

CREATE TABLE `wishlists` (
                             `id` varchar(7) NOT NULL,
                             `item_id` varchar(7) NOT NULL,
                             `user_id` varchar(7) NOT NULL,
                             `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `items`
--
ALTER TABLE `items`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `offers`
--
ALTER TABLE `offers`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`id`);

--
-- Indexes for table `wishlists`
--
ALTER TABLE `wishlists`
    ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Tempo de geração: 30/07/2025 às 18:07
-- Versão do servidor: 9.1.0
-- Versão do PHP: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `pizzaria_db`
--

DELIMITER $$
--
-- Procedimentos
--
DROP PROCEDURE IF EXISTS `sp_atribuir_entregador`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_atribuir_entregador` (IN `p_id_pedido` BIGINT, IN `p_id_entregador` BIGINT)   BEGIN
    DECLARE v_status_pedido VARCHAR(255);
    DECLARE v_cargo_funcionario VARCHAR(50);

    SELECT status_pedido INTO v_status_pedido FROM pedidos WHERE id = p_id_pedido;
    SELECT cargo INTO v_cargo_funcionario FROM funcionarios WHERE id = p_id_entregador;


    IF v_status_pedido = 'PRONTO PARA ENTREGA' AND v_cargo_funcionario = 'ENTREGADOR' THEN
        UPDATE pedidos
        SET id_funcionario_entregador = p_id_entregador,
            status_pedido = 'SAIU PARA ENTREGA'
        WHERE id = p_id_pedido;
    END IF;
END$$

DROP PROCEDURE IF EXISTS `sp_cancelar_pedido`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_cancelar_pedido` (IN `p_id_pedido` BIGINT)   BEGIN
    
    UPDATE pedidos
    SET status_pedido = 'CANCELADO'
    WHERE id = p_id_pedido AND status_pedido = 'RECEBIDO';
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estrutura para tabela `clientes`
--

DROP TABLE IF EXISTS `clientes`;
CREATE TABLE IF NOT EXISTS `clientes` (
  `id` bigint NOT NULL,
  `telefone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `clientes`
--

INSERT INTO `clientes` (`id`, `telefone`) VALUES
(7, '77999689532'),
(9, '77999869632'),
(10, '77999508622'),
(11, '7755698523'),
(15, '77999489591'),
(16, '77999242092'),
(17, '77998632514'),
(18, '77999648591'),
(19, '77999452398'),
(20, '77999563298'),
(21, '77999569832');

-- --------------------------------------------------------

--
-- Estrutura para tabela `enderecos`
--

DROP TABLE IF EXISTS `enderecos`;
CREATE TABLE IF NOT EXISTS `enderecos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_usuario` bigint NOT NULL,
  `rua` varchar(255) NOT NULL,
  `numero` varchar(255) NOT NULL,
  `bairro` varchar(255) NOT NULL,
  `cidade` varchar(255) NOT NULL,
  `cep` varchar(255) NOT NULL,
  `complemento` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_endereco_usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `enderecos`
--

INSERT INTO `enderecos` (`id`, `id_usuario`, `rua`, `numero`, `bairro`, `cidade`, `cep`, `complemento`) VALUES
(11, 7, 'Avenida novo horizonte', '2236', 'Novo horizonte', 'Formosa Do Rio Preto ', '47990000', '');

-- --------------------------------------------------------

--
-- Estrutura para tabela `funcionarios`
--

DROP TABLE IF EXISTS `funcionarios`;
CREATE TABLE IF NOT EXISTS `funcionarios` (
  `id` bigint NOT NULL,
  `cargo` varchar(50) NOT NULL,
  `salario` decimal(38,2) DEFAULT NULL,
  `data_admissao` date DEFAULT NULL,
  `data_demissao` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `funcionarios`
--

INSERT INTO `funcionarios` (`id`, `cargo`, `salario`, `data_admissao`, `data_demissao`) VALUES
(3, 'GERENTE', NULL, NULL, NULL),
(4, 'ENTREGADOR', NULL, NULL, NULL),
(14, 'CAIXA', 12000.00, '2025-07-28', NULL),
(22, 'CAIXA', 1500.00, '2025-07-30', NULL),
(23, 'ENTREGADOR', 1500.00, '2025-07-30', NULL),
(24, 'GERENTE', NULL, NULL, NULL),
(25, 'ENTREGADOR', 1600.00, '2025-07-30', NULL),
(26, 'CAIXA', 1512.00, '2025-07-30', NULL),
(27, 'ENTREGADOR', 1250.00, '2025-07-25', NULL),
(28, 'GERENTE', 1900.00, '2025-07-21', NULL),
(29, 'ENTREGADOR', 1250.00, '2025-07-22', NULL);

-- --------------------------------------------------------

--
-- Estrutura para tabela `itens_pedido`
--

DROP TABLE IF EXISTS `itens_pedido`;
CREATE TABLE IF NOT EXISTS `itens_pedido` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_pedido` bigint NOT NULL,
  `id_produto` bigint NOT NULL,
  `quantidade` int NOT NULL,
  `preco_unitario` decimal(38,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_item_pedido` (`id_pedido`),
  KEY `fk_item_produto` (`id_produto`)
) ENGINE=InnoDB AUTO_INCREMENT=236 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `itens_pedido`
--

INSERT INTO `itens_pedido` (`id`, `id_pedido`, `id_produto`, `quantidade`, `preco_unitario`) VALUES
(225, 137, 203, 1, 48.00),
(226, 137, 209, 1, 42.00),
(227, 138, 203, 1, 48.00),
(228, 139, 203, 1, 48.00),
(229, 139, 209, 1, 42.00),
(230, 139, 212, 1, 10.00),
(231, 140, 203, 1, 48.00),
(232, 140, 202, 1, 50.00),
(233, 140, 201, 1, 45.00),
(234, 141, 202, 1, 50.00),
(235, 141, 219, 2, 48.00);

-- --------------------------------------------------------

--
-- Estrutura para tabela `log_status_pedido`
--

DROP TABLE IF EXISTS `log_status_pedido`;
CREATE TABLE IF NOT EXISTS `log_status_pedido` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pedido_id` bigint NOT NULL,
  `status_antigo` varchar(255) DEFAULT NULL,
  `status_novo` varchar(255) DEFAULT NULL,
  `data_modificacao` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `pedido_id` (`pedido_id`)
) ENGINE=MyISAM AUTO_INCREMENT=151 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `log_status_pedido`
--

INSERT INTO `log_status_pedido` (`id`, `pedido_id`, `status_antigo`, `status_novo`, `data_modificacao`) VALUES
(150, 141, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-30 14:57:59'),
(149, 141, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-30 14:57:51'),
(148, 141, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-30 14:57:37'),
(147, 139, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-30 13:10:16'),
(146, 139, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-30 13:10:14'),
(145, 139, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-30 12:52:52'),
(144, 140, 'RECEBIDO', 'CANCELADO', '2025-07-30 12:38:43'),
(143, 137, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-28 20:11:58'),
(142, 137, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-28 20:11:40'),
(141, 138, 'RECEBIDO', 'CANCELADO', '2025-07-28 20:11:09'),
(140, 137, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-28 20:10:09'),
(26, 88, 'PRONTO PARA ENTREGA', 'RECEBIDO', '2025-07-18 17:44:22'),
(27, 89, 'SAIU PARA ENTREGA', 'RECEBIDO', '2025-07-18 17:45:11'),
(28, 93, 'CANCELADO', 'RECEBIDO', '2025-07-18 17:45:23'),
(29, 92, 'SAIU PARA ENTREGA', 'RECEBIDO', '2025-07-18 17:45:31'),
(30, 90, 'SAIU PARA ENTREGA', 'RECEBIDO', '2025-07-18 17:45:39'),
(31, 91, 'SAIU PARA ENTREGA', 'RECEBIDO', '2025-07-18 17:45:46'),
(32, 94, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-18 17:53:07'),
(33, 94, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-18 17:53:27'),
(34, 94, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-18 17:53:37'),
(35, 95, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-18 17:55:11'),
(36, 95, 'PRONTO PARA ENTREGA', 'RECEBIDO', '2025-07-18 18:22:41'),
(37, 95, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-18 18:23:31'),
(38, 95, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-18 18:24:37'),
(39, 95, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-18 18:25:02'),
(40, 97, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-18 18:25:24'),
(41, 96, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-18 18:25:48'),
(42, 96, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-18 18:26:00'),
(43, 97, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-18 18:26:01'),
(44, 97, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-18 18:26:22'),
(45, 96, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-18 18:26:23'),
(46, 99, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-18 18:32:18'),
(47, 100, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-18 18:32:20'),
(48, 98, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-18 18:32:23'),
(49, 99, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-18 18:32:34'),
(50, 98, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-18 18:32:35'),
(51, 100, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-18 18:32:36'),
(52, 101, 'RECEBIDO', 'CANCELADO', '2025-07-18 18:38:29'),
(53, 102, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-18 19:01:29'),
(54, 103, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-18 19:03:21'),
(55, 103, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-18 19:04:04'),
(56, 104, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-18 19:04:19'),
(57, 103, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-18 19:23:49'),
(58, 105, 'RECEBIDO', 'CANCELADO', '2025-07-18 19:24:40'),
(59, 106, 'RECEBIDO', 'CANCELADO', '2025-07-18 19:25:36'),
(60, 107, 'RECEBIDO', 'CANCELADO', '2025-07-18 19:25:46'),
(61, 108, 'RECEBIDO', 'CANCELADO', '2025-07-18 19:25:52'),
(62, 109, 'RECEBIDO', 'CANCELADO', '2025-07-18 19:25:59'),
(63, 104, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-18 19:36:43'),
(64, 104, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-18 19:36:45'),
(65, 110, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-18 19:37:17'),
(66, 110, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-18 19:37:21'),
(67, 110, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-18 19:37:24'),
(68, 111, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-19 12:24:56'),
(69, 111, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-19 12:25:09'),
(70, 111, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-19 12:25:12'),
(71, 113, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-19 15:17:25'),
(72, 112, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-19 15:17:33'),
(73, 112, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-19 15:17:48'),
(74, 113, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-19 15:17:49'),
(75, 112, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-19 15:17:58'),
(76, 113, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-19 15:18:07'),
(77, 114, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-19 15:30:30'),
(78, 114, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-19 15:30:43'),
(79, 114, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-19 15:30:54'),
(80, 115, 'RECEBIDO', 'CANCELADO', '2025-07-19 16:01:46'),
(81, 116, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-19 16:08:21'),
(82, 116, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-19 16:08:46'),
(83, 116, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-19 16:09:15'),
(84, 117, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-19 17:33:50'),
(85, 117, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-19 17:33:59'),
(86, 117, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-19 17:34:45'),
(87, 118, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-19 17:41:02'),
(88, 118, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-19 17:41:24'),
(89, 118, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-19 17:41:46'),
(90, 119, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-19 18:13:26'),
(91, 119, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-19 18:13:49'),
(92, 119, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-19 18:13:58'),
(93, 120, 'RECEBIDO', 'CANCELADO', '2025-07-19 18:18:28'),
(94, 121, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-21 20:19:28'),
(95, 121, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-21 20:20:08'),
(96, 121, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-21 20:20:35'),
(97, 122, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-21 21:52:30'),
(98, 122, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-21 21:52:56'),
(99, 122, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-21 21:53:21'),
(100, 123, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-23 19:09:27'),
(101, 123, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-23 19:10:02'),
(102, 127, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-23 19:11:41'),
(103, 126, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-23 19:11:45'),
(104, 124, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-23 19:11:49'),
(105, 125, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-23 19:11:53'),
(106, 123, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-23 19:12:16'),
(107, 124, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-23 19:12:19'),
(108, 125, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-23 19:12:20'),
(109, 126, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-23 19:12:22'),
(110, 127, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-23 19:12:22'),
(111, 124, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-23 19:14:56'),
(112, 125, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-23 19:15:00'),
(113, 127, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-23 19:15:03'),
(114, 126, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-23 19:15:06'),
(115, 128, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-24 13:43:42'),
(116, 129, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-24 14:07:43'),
(117, 129, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-24 14:20:30'),
(118, 128, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-24 14:20:31'),
(119, 128, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-24 14:20:35'),
(120, 129, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-24 14:20:39'),
(121, 130, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-24 14:43:45'),
(122, 130, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-24 14:43:54'),
(123, 130, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-24 14:44:10'),
(124, 132, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-24 14:51:31'),
(125, 132, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-24 14:51:40'),
(126, 132, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-24 14:51:52'),
(127, 131, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-24 19:10:24'),
(128, 133, 'RECEBIDO', 'CANCELADO', '2025-07-24 20:07:10'),
(129, 131, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-24 20:09:31'),
(130, 134, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-24 20:18:14'),
(131, 135, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-24 21:46:05'),
(132, 134, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-24 21:46:23'),
(133, 135, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-24 21:46:46'),
(134, 134, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-24 21:46:53'),
(135, 131, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-24 21:46:57'),
(136, 135, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-24 21:47:02'),
(137, 136, 'RECEBIDO', 'PRONTO PARA ENTREGA', '2025-07-24 21:50:02'),
(138, 136, 'PRONTO PARA ENTREGA', 'SAIU PARA ENTREGA', '2025-07-28 08:13:08'),
(139, 136, 'SAIU PARA ENTREGA', 'ENTREGUE', '2025-07-28 08:13:32');

-- --------------------------------------------------------

--
-- Estrutura para tabela `pedidos`
--

DROP TABLE IF EXISTS `pedidos`;
CREATE TABLE IF NOT EXISTS `pedidos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_cliente` bigint NOT NULL,
  `id_endereco_entrega` bigint NOT NULL,
  `id_funcionario_entregador` bigint DEFAULT NULL,
  `data_hora` datetime NOT NULL,
  `valor_total` decimal(38,2) NOT NULL,
  `status_pedido` varchar(255) NOT NULL,
  `pagamento` varchar(255) NOT NULL,
  `tipo_entrega` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pedido_cliente` (`id_cliente`),
  KEY `fk_pedido_endereco` (`id_endereco_entrega`),
  KEY `fk_pedido_funcionario` (`id_funcionario_entregador`)
) ENGINE=InnoDB AUTO_INCREMENT=142 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `pedidos`
--

INSERT INTO `pedidos` (`id`, `id_cliente`, `id_endereco_entrega`, `id_funcionario_entregador`, `data_hora`, `valor_total`, `status_pedido`, `pagamento`, `tipo_entrega`) VALUES
(137, 7, 11, 4, '2025-07-28 23:09:34', 95.00, 'ENTREGUE', 'CARTAO_CREDITO', 'DELIVERY'),
(138, 7, 11, NULL, '2025-07-28 23:10:48', 53.00, 'CANCELADO', 'DINHEIRO', 'DELIVERY'),
(139, 7, 11, 4, '2025-07-30 14:11:16', 105.00, 'ENTREGUE', 'DINHEIRO', 'DELIVERY'),
(140, 7, 11, NULL, '2025-07-30 15:33:51', 148.00, 'CANCELADO', 'CARTAO_CREDITO', 'DELIVERY'),
(141, 7, 11, 4, '2025-07-30 17:56:57', 151.00, 'ENTREGUE', 'DINHEIRO', 'DELIVERY');

--
-- Acionadores `pedidos`
--
DROP TRIGGER IF EXISTS `trg_log_mudanca_status`;
DELIMITER $$
CREATE TRIGGER `trg_log_mudanca_status` AFTER UPDATE ON `pedidos` FOR EACH ROW BEGIN
    
    IF OLD.status_pedido <> NEW.status_pedido THEN
        INSERT INTO log_status_pedido (pedido_id, status_antigo, status_novo)
        VALUES (OLD.id, OLD.status_pedido, NEW.status_pedido);
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estrutura para tabela `produtos`
--

DROP TABLE IF EXISTS `produtos`;
CREATE TABLE IF NOT EXISTS `produtos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) DEFAULT NULL,
  `descricao` longtext,
  `preco` decimal(38,2) DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `tamanho` varchar(255) DEFAULT NULL,
  `caminho_imagem` varchar(255) DEFAULT NULL,
  `ativo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=223 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `produtos`
--

INSERT INTO `produtos` (`id`, `nome`, `descricao`, `preco`, `tipo`, `tamanho`, `caminho_imagem`, `ativo`) VALUES
(201, 'Pizza de Calabresa', 'Molho de tomate fresco, queijo mussarela, fatias de calabresa e cebola.', 45.00, 'PIZZA_SALGADA', 'GRANDE', 'https://pizzaria-imagens-delivery.s3.sa-east-1.amazonaws.com/0abc102d-d87d-4b82-a6a7-6947f81647d9_pizza_calabresa.png', 1),
(202, 'Pizza 4 Queijos', 'Molho de tomate, queijo mussarela, provolone, parmesão e gorgonzola.', 50.00, 'PIZZA_SALGADA', 'GRANDE', 'https://pizzaria-imagens-delivery.s3.sa-east-1.amazonaws.com/0115001a-8b09-471c-bb2e-2ab424683e57_pizza_4_queijos.png', 1),
(203, 'Pizza de Frango com Catupiry', 'Molho de tomate, queijo mussarela, frango desfiado e catupiry original.', 48.00, 'PIZZA_SALGADA', 'MÉDIA', 'https://pizzaria-imagens-delivery.s3.sa-east-1.amazonaws.com/6b38dacc-9944-4593-afaf-43ae860e6565_pizza_frango_catupiry.png', 1),
(207, 'Pizza de Carne Seca', 'Carne seca, queijo, catupiry e oregano', 45.00, 'PIZZA SALGADA', 'MEDIO', 'https://pizzaria-imagens-delivery.s3.sa-east-1.amazonaws.com/70d5369e-615e-47c5-9d95-ffadf99e6e1b_CarneSeca.png', 1),
(209, 'Pizza de Chocolate com Morango', 'Delicioso chocolate ao leite com morangos frescos fatiados.', 42.00, 'PIZZA_DOCE', 'PEQUENA', 'https://pizzaria-imagens-delivery.s3.sa-east-1.amazonaws.com/26f54192-accf-4ec4-9d2f-ed7e9f348ad3_pizza_chocolate_morango.png', 1),
(210, 'Pizza de KIT KAT', 'Brigadeiro, chocolate granulado, pedaços de kit kat, fatias de morango e borda recheada com chocolate meio amargo.', 36.90, 'PIZZA_DOCE', 'GRANDE', 'https://pizzaria-imagens-delivery.s3.sa-east-1.amazonaws.com/071b2ff2-af7d-4193-90cc-4e9a1a97d204_pizzaKitKat.png', 1),
(212, 'Coca cola 2L', 'Escolha entre os sabores disponíveis.', 10.00, 'BEBIDA', 'ÚNICO', 'https://pizzaria-imagens-delivery.s3.sa-east-1.amazonaws.com/0e949a13-0bb1-40ac-8d27-b553ccdf556c_Cocacola2L.png', 1),
(217, 'Guarana 2 Litros', 'Refrigerante de Guarana', 10.00, 'BEBIDA', 'UNICO', 'https://pizzaria-imagens-delivery.s3.sa-east-1.amazonaws.com/f1a76235-54f3-4114-9907-148500acbe98_Litro_de_Guarana_Antarctica_2L_PNG_Transparente_Sem_Fundo.png', 1),
(218, 'Pepsi 2 Litros', 'Refrigerante cola', 10.00, 'BEBIDA', 'UNICO', 'https://pizzaria-imagens-delivery.s3.sa-east-1.amazonaws.com/e0df2c9a-255c-4454-a9c4-f2a0b112b1cb_d416af9646f567ce75a1006891b73cdf_refrigerante-pepsi-2l_lett_1-removebg-preview.png', 1),
(219, 'Hamburguer Triplo artesanal', 'Três carnes 180g, bacon, queijo cheddar, molho barbecue, alface e pepino ', 48.00, 'HAMBURGUER', 'ÚNICO', 'https://pizzaria-imagens-delivery.s3.sa-east-1.amazonaws.com/d5663937-eab7-4a82-93dc-f10a21070761_criativo-65d7c89c4961eMjIvMDIvMjAyNCAxOWgyMA__-removebg-preview.png', 1);

-- --------------------------------------------------------

--
-- Estrutura para tabela `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `senha` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Despejando dados para a tabela `usuarios`
--

INSERT INTO `usuarios` (`id`, `nome`, `email`, `senha`) VALUES
(1, 'guilherme', 'coradodasilva33@gmail.com', '$2a$10$WHndeFWppqdRBliVBWnvVO.Pt.O0hGADIceUgEw4MyDbZ5jh4OBym'),
(2, 'Guilherme Alves corado', 'coradodasilva32@gmail.com', '$2a$10$FUCg7h8xQmGSiyuqv30eaOEvj38.dEZ7aPdiwntdl9ltHK7hseMI2'),
(3, 'ana maria', 'aninha@gmail.com', '$2a$10$yjUWCT60dGbivoZM4vxUWurikNCPukRTW5a2LS1PRyzz7tr4MVLeS'),
(4, 'jose', 'jose@gmail.com', '$2a$10$kGO1rmy7ABT9QsqsnGJgcucg7ovikR5mlADeOUB7OUvkvzswB9mDu'),
(5, 'Geysa', 'geysa@gmail.com', '$2a$10$xtbCPu8FPtZdrmhSEHdnqOuq0Ai0Kh8Rw9Qfn9oJ7BP/6ihB4Co3C'),
(6, 'João Entregador', 'entregador@pizzaria.com', '$2a$10$3OK.Bjn5oF4wJAbYgX.v9.VzYj60kudjPikV5yO5bIIc2tPUMcn/a'),
(7, 'Guilherme', 'gui@gmail.com', '$2a$10$P2Of8Fq7P2BYDU/0T.TM2.ZtJlmL58OMeQbGfaqqu6/SBtSRKwJKm'),
(8, 'bruno silva ', 'bru@gmail.com', '$2a$10$Ci8wiDVpUtI7/BwVtco1u.D0IUXNhL.f4Fv3MRqW7LHQ8zVPSdydu'),
(9, 'marcelo', 'marcelo@gmail.com', '$2a$10$IafioDx3OxYcJz18rRrJSO0cuzvFb9oQiYJ1fSJ3buUOdXflADlmO'),
(10, 'natalia', 'nat@gmail.com', '$2a$10$CqIk/UtKZnmWMbaKr2kqveqMfEV1T.LCir.RNKYYCG8K1NA0f6EqC'),
(11, 'Lucas', 'luc@gmail.com', '$2a$10$PeFqZXTD1jmOvvM8xOe/R.MITZGkCvUAOCmV1vOOiYiyTqVWX/iHa'),
(12, 'bruno', 'bruno@gmail.com', '$2a$10$peeGgUevbD4UjptBhmKDDOrO8J2swgnXAvsPenAgZW.NAlQTFVJcS'),
(13, 'geysa', 'g@gamail.com', '$2a$10$5QVAvTZXHfNQLLlW6LumfeKSrs8cQVEJn7ftHfbe5lzd8G2WZTRvW'),
(14, 'guilherem', 'corado@gmail.com', '$2a$10$WdK2/9ha4avZ4xG9kcbTSOlx0.Ag5.8gSxW3glwW6phEK1kIAviWO'),
(15, 'Roberto', 'rob@gmail.com', '$2a$10$S1hecaM7UdmxD.4.Z1yL5uliL0ohO8/C8lyJl7QjDBigGYDKwIanO'),
(16, 'Mariana', 'mari@gmail.com', '$2a$10$euG8gO.z.URoohRVYTKVH.yNz08k2K8A3T08gErmkqpNwZu1WqeCW'),
(17, 'Emerson Silva', 'Silva@gmail.com', '$2a$10$Yza.8mhcXCQARHUSCD8UjO1QJ9tUVRHLYCqNTMpS2c26nXK/hLZxW'),
(18, 'Luiz Correia', 'Luiz@gmail.com', '$2a$10$fU2LBBRmJ9ddxS9V.yORYehEsyNbYeRV80SFLyUM2iCGxa7SGZaDC'),
(19, 'Fernanda Ferreira', 'Fe@gmail.com', '$2a$10$4tYZOr1Ly8Z8nIm42LbOdujrdExsvB0fFXoCppHBTHdQJLHjHfL0O'),
(20, 'Emanuel Alves', 'Emanu@gmail.com', '$2a$10$dfeGSwyURIvwl1Xd6kECRu8eFoIACJqVka8PicOQfTK87UK8gAkyK'),
(21, 'Emilly Fernandes', 'Emill@gmail.com', '$2a$10$SL8uYzUSMZmcJmNX5y4meubdeZ4up3rLjmI1NNpXcdt8QnOyxT3bS'),
(22, 'Roberta', 'robet@gmail.com', '$2a$10$LYnGsZORmVk/qfxyIQ95JO.hdujpzc5ptCIAq/AcT6cYYWf5/Als2'),
(23, 'Ana Pereira', 'Ani@gmail.com', '$2a$10$YLtfzF47hHqxVyIJ0gkigOrM5l1Qg4xYFqKaaTWpoG2HshcnnpY1S'),
(24, 'Heitor Carlos', 'Carlos@gmail.com', '$2a$10$32HNlcy4RafZuR3qNC5HTeN4iZFcA6aD9dA7fiJazp3yxNYwjNtTS'),
(25, 'Fabricio andrade', 'andrade@gmail.com', '$2a$10$hz570ZmwKAz/Okcpv93InetyrARr7wvR6eLHepYWeGPhbdC1ShjOK'),
(26, 'Natalia Valente', 'Valente@gmail.com', '$2a$10$dVNYqjF38pwdmRjPeQny8efb3qnbg2M05ELs2/FkqbGDPdFuFRMna'),
(27, 'Victor Ferreira', 'ferreira@gmail.com', '$2a$10$AaIyFkChICdi939GMoy8WOKmVA6NGbkBEWi7iOnDdz3jb6evwTea2'),
(28, 'Ana Heloisa Almeida', 'helo@gmail.com', '$2a$10$hIJ5/Yw6x0VMbzHcnaNJL./xQmAZNZsK3TYawqajm0ekJID10rYIa'),
(29, 'Samuel Noronha', 'noronha@gmail.com', '$2a$10$063wbdCrjdH21Cza1JybEODcYk0ZK3XkF84GYrxrTE3YfbayfcZtG');

-- --------------------------------------------------------

--
-- Estrutura stand-in para view `v_pedidos_completos`
-- (Veja abaixo para a visão atual)
--
DROP VIEW IF EXISTS `v_pedidos_completos`;
CREATE TABLE IF NOT EXISTS `v_pedidos_completos` (
`pedido_id` bigint
,`data_hora` datetime
,`valor_total` decimal(38,2)
,`status_pedido` varchar(255)
,`pagamento` varchar(255)
,`cliente_nome` varchar(255)
,`rua_entrega` varchar(255)
,`numero_entrega` varchar(255)
,`bairro_entrega` varchar(255)
,`entregador_id` bigint
,`entregador_nome` varchar(255)
);

-- --------------------------------------------------------

--
-- Estrutura para view `v_pedidos_completos`
--
DROP TABLE IF EXISTS `v_pedidos_completos`;

DROP VIEW IF EXISTS `v_pedidos_completos`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_pedidos_completos`  AS SELECT `p`.`id` AS `pedido_id`, `p`.`data_hora` AS `data_hora`, `p`.`valor_total` AS `valor_total`, `p`.`status_pedido` AS `status_pedido`, `p`.`pagamento` AS `pagamento`, `u`.`nome` AS `cliente_nome`, `e`.`rua` AS `rua_entrega`, `e`.`numero` AS `numero_entrega`, `e`.`bairro` AS `bairro_entrega`, `p`.`id_funcionario_entregador` AS `entregador_id`, `f_user`.`nome` AS `entregador_nome` FROM (((((`pedidos` `p` join `clientes` `c` on((`p`.`id_cliente` = `c`.`id`))) join `usuarios` `u` on((`c`.`id` = `u`.`id`))) join `enderecos` `e` on((`p`.`id_endereco_entrega` = `e`.`id`))) left join `funcionarios` `f` on((`p`.`id_funcionario_entregador` = `f`.`id`))) left join `usuarios` `f_user` on((`f`.`id` = `f_user`.`id`))) ;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `clientes`
--
ALTER TABLE `clientes`
  ADD CONSTRAINT `fk_cliente_usuario` FOREIGN KEY (`id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;

--
-- Restrições para tabelas `enderecos`
--
ALTER TABLE `enderecos`
  ADD CONSTRAINT `fk_endereco_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;

--
-- Restrições para tabelas `funcionarios`
--
ALTER TABLE `funcionarios`
  ADD CONSTRAINT `fk_funcionario_usuario` FOREIGN KEY (`id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;

--
-- Restrições para tabelas `itens_pedido`
--
ALTER TABLE `itens_pedido`
  ADD CONSTRAINT `fk_item_pedido` FOREIGN KEY (`id_pedido`) REFERENCES `pedidos` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_item_produto` FOREIGN KEY (`id_produto`) REFERENCES `produtos` (`id`) ON DELETE RESTRICT;

--
-- Restrições para tabelas `pedidos`
--
ALTER TABLE `pedidos`
  ADD CONSTRAINT `fk_pedido_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id`) ON DELETE RESTRICT,
  ADD CONSTRAINT `fk_pedido_endereco` FOREIGN KEY (`id_endereco_entrega`) REFERENCES `enderecos` (`id`) ON DELETE RESTRICT,
  ADD CONSTRAINT `fk_pedido_funcionario` FOREIGN KEY (`id_funcionario_entregador`) REFERENCES `funcionarios` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

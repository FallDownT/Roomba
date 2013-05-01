----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    14:46:47 04/13/2013 
-- Design Name: 
-- Module Name:    bluetoothControlTL - Behavioral 
-- Project Name: 
-- Target Devices: 
-- Tool versions: 
-- Description: 
--
-- Dependencies: 
--
-- Revision: 
-- Revision 0.01 - File Created
-- Additional Comments: 
--
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity bluetoothControlTL is
port(
	clock 				: in std_logic;
	temp_send_toggle	: in std_logic;
	uart_rx				: in std_logic;
	irrf_range			: in std_logic_vector(7 downto 0);
	irrf_servopos		: in std_logic_vector(7 downto 0);
	new_range_data		: in std_logic;
	uart_tx				: out std_logic;
	seven_seg_an		: out std_logic_vector(3 downto 0);
	seven_seg_cath 	: out std_logic_vector(7 downto 0);
	motor_1_out			: out std_logic_vector(7 downto 0);
	motor_2_out			: out std_logic_vector(7 downto 0);
	txclk_test			: out std_logic;
	do_transmit_test	: out std_logic;
	state_test			: out std_logic_vector(1 downto 0)
	);	
end bluetoothControlTL;

architecture Behavioral of bluetoothControlTL is
signal txcount : integer range 0 to 867;
signal rxcount : integer range 0 to 53;
signal recieve_reg, transmit_reg : std_logic_vector(7 downto 0);
signal motor_1, motor_2, temp_1, temp_2 : std_logic_vector(7 downto 0):=x"00";
signal txclkw, tx_emptyw, rxclkw, ld_tx_dataw, rx_emptyw, new_data_to_send, temp : std_logic;
signal to_seven_seg : std_logic_vector(7 downto 0);
signal compare_reg : std_logic_vector(15 downto 0);

signal synced_new_range_data : std_logic; --synchronized signal from oopic. toggles state when a new set of data is ready to be processed
signal new_irrf_dataset_pulse : std_logic; --pulse created on system clock. pulses high when above signa switches logic level
signal do_transmit : std_logic; --goes high when a data transmission must begin. is brought low when all bytes are transmitted
signal tx_designator_select : std_logic_vector(1 downto 0):="00"; --state poisition counter
signal ndreg : std_logic_vector(1 downto 0);

signal enable  : std_logic;

component bluetoothUART is
    port (
        reset       :in  std_logic;
        txclk       :in  std_logic;
        ld_tx_data  :in  std_logic;
        tx_data     :in  std_logic_vector (7 downto 0);
        tx_enable   :in  std_logic;
        tx_out      :out std_logic;
        tx_empty    :out std_logic;
        rxclk       :in  std_logic;
        uld_rx_data :in  std_logic;
        rx_data     :out std_logic_vector (7 downto 0);
        rx_enable   :in  std_logic;
        rx_in       :in  std_logic;
        rx_empty    :out std_logic
    );
end component;

component Display_To_Nexys3_SSD is
	Port(
		clk						: in std_logic;
		to_seven_seg			: in std_logic_vector (7 downto 0);
		active_an				: out std_logic_vector (3 downto 0);
		active_cath				: out std_logic_vector (7 downto 0)
	);
end component;

begin
motor_1_out <= motor_1;
motor_2_out <= motor_2;
txclk_test <= txclkw;
do_transmit_test <= do_transmit;
state_test <= tx_designator_select;

--transmit_clock
--changed to 115.2k baud
process(clock)
begin
if rising_edge(clock) then
	if (txcount = 867) then
		txcount <= 0;
		txclkw <= '1';
	else
		txcount <= txcount + 1;
		txclkw <= '0';
	end if;
end if;
end process;

--recieve_clock
--changed to 115.2kx16 baud
process(clock)
begin
	if rising_edge(clock) then
		if (rxcount = 53) then
			rxcount <= 0;
			rxclkw <= '1';
		else
			rxcount <= rxcount + 1;
			rxclkw <= '0';
		end if;
	end if;
end process;

--process(clock, txclkw)
--begin
--	if rising_edge(clock) and txclkw = '1' then
--		if tx_emptyw = '1' then
--			ld_tx_dataw <= '1';
--			transmit_reg <= x"58";
--		else
--			ld_tx_dataw <= '0';
--		end if;
--	end if;
--end process;

--transmitting data
process(clock)
begin
	if rising_edge(clock) then	
		if txclkw = '1' then
			--if do_transmit = '1' and tx_emptyw = '1' then
			if tx_emptyw = '1' then
				case tx_designator_select is
					when "00" =>
						transmit_reg <= x"41";  --A: angle data designator
						tx_designator_select <= "01";
					when "01" =>
						transmit_reg <= irrf_servopos; --angle data
						tx_designator_select <= "10";
					when "10" =>
						transmit_reg <= irrf_range; --range data
						tx_designator_select <= "00";
					when others =>
						transmit_reg <= x"58";
						tx_designator_select <= "00";
				end case;
			end if;
		end if;
	end if;
end process;	

--recieving data
process(clock, txclkw)
begin
	if rising_edge(clock) then
		if rx_emptyw = '0' then
			compare_reg <= recieve_reg & compare_reg(15 downto 8);
			
			if compare_reg(7 downto 0) = x"41" then
				temp_1 <= compare_reg(15 downto 8);
			elsif compare_reg(7 downto 0) = x"42" then
				temp_2 <= compare_reg(15 downto 8);
			end if;
			
			case temp_1 is
				when x"30" => 
					to_seven_seg(3 downto 0) <= x"0";
					motor_1 <= "10000000";
				when x"31" => 
					to_seven_seg(3 downto 0) <= x"1";
					motor_1 <= "10001111";
				when x"32" => 
					to_seven_seg(3 downto 0) <= x"2";
					motor_1 <= "10101111";
				when x"33" => 
					to_seven_seg(3 downto 0) <= x"3";
					motor_1 <= "10101111";
				when x"34" => 
					to_seven_seg(3 downto 0) <= x"4";
					motor_1 <= "00000000";
				when x"35" => 
					to_seven_seg(3 downto 0) <= x"5";
					motor_1 <= "00100011";
				when x"36" => 
					to_seven_seg(3 downto 0) <= x"6";
					motor_1 <= "00111111";
				when x"37" => 
					to_seven_seg(3 downto 0) <= x"7";
					motor_1 <= "01010000";
				when x"38" => 
					to_seven_seg(3 downto 0) <= x"8";
					motor_1 <= "01110000";
				when x"39" => 
					to_seven_seg(3 downto 0) <= x"9";
					motor_1 <= "01111111";
				when others=> 
					to_seven_seg(3 downto 0) <= "0000";
					motor_1 <= "00000000";
			end case;
			
			case temp_2 is
				when x"30" => 
					to_seven_seg(7 downto 4) <= x"0";
					motor_2 <= "10000000";
				when x"31" => 
					to_seven_seg(7 downto 4) <= x"1";
					motor_2 <= "10001111";
				when x"32" => 
					to_seven_seg(7 downto 4) <= x"2";
					motor_2 <= "10101111";
				when x"33" => 
					to_seven_seg(7 downto 4) <= x"3";
					motor_2 <= "10101111";
				when x"34" => 
					to_seven_seg(7 downto 4) <= x"4";
					motor_2 <= "00000000";
				when x"35" => 
					to_seven_seg(7 downto 4) <= x"5";
					motor_2 <= "00100011";
				when x"36" => 
					to_seven_seg(7 downto 4) <= x"6";
					motor_2 <= "00111111";
				when x"37" => 
					to_seven_seg(7 downto 4) <= x"7";
					motor_2 <= "01010000";
				when x"38" => 
					to_seven_seg(7 downto 4) <= x"8";
					motor_2 <= "01110000";
				when x"39" => 
					to_seven_seg(7 downto 4) <= x"9";
					motor_2 <= "01111111";
				when others=> 
					to_seven_seg(7 downto 4) <= "0000";
					motor_2 <= "00000000";
			end case;
			
		end if;
	end if;
end process;

inst_uart:bluetoothUART
port map(
	reset => '0',
	--tx
	txclk => txclkw, 
	ld_tx_data => '1',--ld_tx_dataw,
	tx_data => transmit_reg,
	tx_enable => temp_send_toggle,
	tx_out => uart_tx,
	tx_empty => tx_emptyw,
	--rx
	rxclk => rxclkw,
	uld_rx_data => '1',
	rx_data => recieve_reg,
	rx_enable => '1',
	rx_in => uart_rx,
	rx_empty => rx_emptyw
	);
	
inst_display:Display_To_Nexys3_SSD
port map(
	clk => clock,
	to_seven_seg => to_seven_seg,
	active_an => seven_seg_an,
	active_cath => seven_seg_cath
	);

end Behavioral;


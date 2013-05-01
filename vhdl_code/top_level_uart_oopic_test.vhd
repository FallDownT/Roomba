----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    13:11:15 04/16/2013 
-- Design Name: 
-- Module Name:    top_level_uart_oopic_test - Behavioral 
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

entity top_level_uart_oopic_test is
port(
	sysClock:			in std_logic;	
	LED:					out std_logic_vector(7 downto 0);
	switch:			in std_logic;
	serialDataOutright:		out std_logic;
	flow_controlright:		in std_logic;
	serialDataOutleft:		out std_logic;
	flow_controlleft:		in std_logic;
	serialdataINir:		in std_logic;
	serialdataINser:		in std_logic;
	newdatatoggle:			in std_logic;
	reset:				in std_logic;
	
	bluetooth_uart_rx : in std_logic;
	bluetooth_uart_tx : out std_logic;
	seven_seg_an		: out std_logic_vector(3 downto 0);
	seven_seg_cath		: out std_logic_vector(7 downto 0);
	
	txclk_test			: out std_logic;
	do_transmit_test	: out std_logic;
	state_test			: out std_logic_vector(1 downto 0)	
);
end top_level_uart_oopic_test;

architecture Behavioral of top_level_uart_oopic_test is
component uart is
    port (
        reset       :in  std_logic;
        txclk       :in  std_logic;
        ld_tx_data  :in  std_logic;
        tx_data     :in  std_logic_vector (7 downto 0);
        tx_enable   :in  std_logic;
        tx_out      :out std_logic;
		  flow_control:in  std_logic;
        tx_empty    :out std_logic;
		  rxclk       :in  std_logic;
        uld_rx_data :in  std_logic;
        rx_data     :out std_logic_vector (7 downto 0);
        rx_enable   :in  std_logic;
        rx_in       :in  std_logic;
        rx_empty    :out std_logic
    );
end component;

component KpClkDiv is
port( 
	CLKin			:	in std_logic;
	CLK_out		:	out std_logic;
--	CLK_out2		:	out std_logic
	CLK_out_rx	:	inout std_logic

);
end component;

component pulse_gen is
port(
	sysClock : in std_logic;
	pulse_enable : in std_logic;
	output_pulse : out std_logic
	);
end component;

component bluetoothControlTL is
port(
	clock 				: in std_logic;
	temp_send_toggle  : in std_logic;
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
end component;

signal rxclk			: std_logic;
signal BaudClockTX	:	std_logic;
signal output_pulseR			: 	std_logic;
signal pulse_enableR			:	std_logic;
signal output_pulseL			: 	std_logic;
signal pulse_enableL			:	std_logic;
signal RXdataIR					:	std_logic_vector(7 downto 0);
signal rxemptyIR					:	std_logic;
signal RXdataSER					:	std_logic_vector(7 downto 0);
signal rxemptySER					:	std_logic;
signal newdatatogglew				:	std_logic;   ------inverts logic level whenever a new set of data arrives from the rangefinder
--signal load				: 	std_logic;

signal motor_1, motor_2 : std_logic_vector(7 downto 0);
signal txclk_testw, do_transmit_testw : std_logic;
signal state_testw : std_logic_vector(1 downto 0);

begin
txclk_test <= newdatatogglew;
do_transmit_test <= do_transmit_testw;
state_test <= state_testw;

--LED <= RXdataIR;
LED <= RXdataSER;
newdatatogglew <= newdatatoggle;

inst_bluetoothControlTL:bluetoothControlTL
port map(
	clock => sysClock,
	temp_send_toggle => switch,
	uart_rx => bluetooth_uart_rx,
	irrf_range => RXDataIR,
	irrf_servopos => RXdataSER,
	uart_tx => bluetooth_uart_tx,
	seven_seg_an => seven_seg_an,
	seven_seg_cath => seven_seg_cath,
	motor_1_out => motor_1,
	motor_2_out => motor_2,
	new_range_data => newdatatogglew,
	txclk_test => txclk_testw,
	do_transmit_test => do_transmit_testw,
	state_test => state_testw
	);
	


Right_UART: uart
   	port map(
        reset       	=> reset,
        txclk        => BaudClockTX,
        ld_tx_data   => '1',
        tx_data    	=> motor_2,
        tx_enable   	=> '1',
        tx_out     	=> serialDataOutright,
		  flow_control => flow_controlright,
        tx_empty     => pulse_enableR,
		  rxclk        => rxclk,
        uld_rx_data  => '1',
        rx_data      => RXDataIR, --distance in 8-bit signed binary from ir rangefinder
        rx_enable    => '1',
        rx_in        => serialdataINir,
        rx_empty     => rxemptyIR
    );
	 
Left_UART: uart
   	port map(
        reset       	=> reset,
        txclk        => BaudClockTX,
        ld_tx_data   => '1',
        tx_data    	=> motor_1,
        tx_enable   	=> '1',
        tx_out     	=> serialDataOutleft,
		  flow_control => flow_controlleft,
        tx_empty     => pulse_enableL,
		  rxclk        => rxclk,
        uld_rx_data  => '1',
        rx_data      => RXDataSER, --servo position in 8-bit signed binary from ir rangefinder
        rx_enable    => '1',
        rx_in        => serialdataINser,
        rx_empty     => rxemptySER
    );
	 
Inst_clock_divider: KpClkDiv
port map( 
	CLKin			=> sysClock,		
	CLK_out		=>	BaudClockTX,
	--CLK_out2	   => txclock
	CLK_out_rx	=> rxclk

);

pulse_genleft : pulse_gen
port map(
	sysClock => sysClock,
	pulse_enable => pulse_enableL,
	output_pulse => output_pulseL
	);
	
pulse_genright : pulse_gen
port map(
	sysClock => sysClock,
	pulse_enable => pulse_enableR,
	output_pulse => output_pulseR
	);



end Behavioral;


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
	switches:			in std_logic_Vector(7 downto 0);
	serialDataOut:		out std_logic;
	flow_control:		in std_logic;
	reset:				in std_logic
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
        tx_empty    :out std_logic
--		  rxclk       :in  std_logic;
--        uld_rx_data :in  std_logic;
--        rx_data     :out std_logic_vector (7 downto 0);
--        rx_enable   :in  std_logic;
--        rx_in       :in  std_logic;
--        rx_empty    :out std_logic
    );
end component;

component KpClkDiv is
port( 
	CLKin			:	in std_logic;
	CLK_out		:	out std_logic
--	CLK_out2		:	out std_logic

);
end component;


signal BaudClockTX	:	std_logic;
signal output_pulse			: 	std_logic;
signal pulse_enable			:	std_logic;
--signal load				: 	std_logic;

begin

LED <= switches;


Inst_UART: uart
   	port map(
        reset       	=> reset,
        txclk        => BaudClockTX,
        ld_tx_data   => output_pulse,
        tx_data    	=> switches,
        tx_enable   	=> '1',
        tx_out     	=> serialDataOut,
		  flow_control => flow_control,
        tx_empty     => pulse_enable
		  --rxclk        => BaudClockRX,
        --uld_rx_data  => '1',
        --rx_data      => RXData,
        --rx_enable    => '1',
        --rx_in        => serialDataIn,
        --rx_empty     => rxempty
    );
	 
Inst_clock_divider: KpClkDiv
port map( 
	CLKin			=> sysClock,		
	CLK_out		=>	BaudClockTX
	--CLK_out2	   => txclock

);



pulse : process (sysClock)
variable lSyncFFs : std_logic_vector(0 to 2);
begin
if (sysClock = '0') and (pulse_enable = '1') then
	output_pulse <= (lSyncFFs(2) and not lSyncFFs(1)) or (lSyncFFs(1) and not lSyncFFs(2));
	lSyncFFs := pulse_enable & lSyncFFs(0 to 1);
elsif (sysClock = '1') and (pulse_enable = '1') then
	output_pulse <= (lSyncFFs(2) and not lSyncFFs(1)) or (lSyncFFs(1) and not lSyncFFs(2));
	lSyncFFs := '0' & lSyncFFs(0 to 1);
elsif (sysClock = '0') and (pulse_enable = '1') then
	output_pulse <= (lSyncFFs(2) and not lSyncFFs(1)) or (lSyncFFs(1) and not lSyncFFs(2));
	lSyncFFs := '0' & lSyncFFs(0 to 1);
elsif (sysClock = '0') and (pulse_enable = '0') then
	output_pulse <= (lSyncFFs(2) and not lSyncFFs(1)) or (lSyncFFs(1) and not lSyncFFs(2));
	lSyncFFs := '0' & lSyncFFs(0 to 1);
end if;
end process;


end Behavioral;


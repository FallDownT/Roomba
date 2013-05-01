----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    19:56:19 04/22/2013 
-- Design Name: 
-- Module Name:    pulse_gen - Behavioral 
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

entity pulse_gen is
port(
	sysClock : in std_logic;
	pulse_enable : in std_logic;
	output_pulse : out std_logic
	);
end pulse_gen;

architecture Behavioral of pulse_gen is

begin

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
	output_pulse<= (lSyncFFs(2) and not lSyncFFs(1)) or (lSyncFFs(1) and not lSyncFFs(2));
	lSyncFFs := '0' & lSyncFFs(0 to 1);
end if;
end process;

end Behavioral;


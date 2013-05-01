library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity Clock_Enabler is
	Generic(
		max_count : integer := 100000000	-- 100MHz
	);
	Port(
		clock		: in std_logic;
		reset		: in std_logic;
		enable	: out std_logic
	);
end Clock_Enabler;

architecture Behavioral of Clock_Enabler is

	signal counter	: integer range 0 to max_count :=0;

begin
	
	-- clock_enabling is a process which turns enable HIGH for one clock pulse
	-- when count reaches max_count.  enable is LOW all other times.
	-- reset resets the counter to 0.
	clock_enabling: process (clock, reset)
	begin
		if reset = '1' then
			counter <= 0;
		elsif rising_edge(clock) then
			if(counter = max_count - 1) then
--			if(counter = 100000000 - 1) then
				counter <= 0;
				enable <= '1';
			else
				counter <= counter + 1;
				enable <= '0';
			end if;
		end if;
	end process;

end Behavioral;

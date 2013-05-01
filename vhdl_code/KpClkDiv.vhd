library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity KpClkDiv is
port( 
	CLKin			:	in std_logic;
	CLK_out		:	out std_logic;
	CLK_out2		:	out std_logic;
	CLK_out_rx	:	inout std_logic
);
end KpClkDiv;

-------------------------------------------------
architecture Behavioral of KpClkDiv is

signal count : integer :=1; 
signal count2:	integer :=1;
signal count3 : integer :=1;
signal sigout	: std_logic := '0';
signal sigout2 : std_Logic := '0';

-------------------------------------------------
begin

------------------------------------------
process(CLKin)
begin
	if(CLKin'event and CLKin='1') then
		count <= count + 1;
		if(count = 5207) then    --<FOR CLKIN 100MZ>--
			sigout <= not sigout;
			count <=1; 
		else
		end if;
	else
	end if;
end process;

CLK_out <= sigout;

process(CLKin)
begin
	if(CLKin'event and CLKin='1') then
		count2 <= count2 + 1;
		if(count2 = 325) then    --<FOR CLKIN 100MZ>--
			CLK_out_rx <= not CLK_out_rx;
			count2 <=1; 
		else
		end if;
	else
	end if;
end process;

--process(CLKin)
--begin
--	if(CLKin'event and CLKin='1') then
--		count2 <= count2 + 1;
--		if(count2 = 520700) then    --<FOR CLKIN 100MZ>--
--			sigout2 <= not sigout2;
--			count2 <=1; 
--		else
--		end if;
--	else
--	end if;
--end process;
--
--CLK_out2 <= sigout2;


------------------------------------------
end Behavioral;


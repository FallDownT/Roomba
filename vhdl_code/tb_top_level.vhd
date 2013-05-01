--------------------------------------------------------------------------------
-- Company: 
-- Engineer:
--
-- Create Date:   10:48:22 04/23/2013
-- Design Name:   
-- Module Name:   C:/Users/Ethan/Documents/uart_oopic_test/tb_top_level.vhd
-- Project Name:  uart_oopic_test
-- Target Device:  
-- Tool versions:  
-- Description:   
-- 
-- VHDL Test Bench Created by ISE for module: top_level_uart_oopic_test
-- 
-- Dependencies:
-- 
-- Revision:
-- Revision 0.01 - File Created
-- Additional Comments:
--
-- Notes: 
-- This testbench has been automatically generated using types std_logic and
-- std_logic_vector for the ports of the unit under test.  Xilinx recommends
-- that these types always be used for the top-level I/O of a design in order
-- to guarantee that the testbench will bind correctly to the post-implementation 
-- simulation model.
--------------------------------------------------------------------------------
LIBRARY ieee;
USE ieee.std_logic_1164.ALL;
 
-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--USE ieee.numeric_std.ALL;
 
ENTITY tb_top_level IS
END tb_top_level;
 
ARCHITECTURE behavior OF tb_top_level IS 
 
    -- Component Declaration for the Unit Under Test (UUT)
 
    COMPONENT top_level_uart_oopic_test
    PORT(
         sysClock : IN  std_logic;
         LED : OUT  std_logic_vector(7 downto 0);
         switches : IN  std_logic_vector(7 downto 0);
         serialDataOutright : OUT  std_logic;
         flow_controlright : IN  std_logic;
         serialDataOutleft : OUT  std_logic;
         flow_controlleft : IN  std_logic;
         reset : IN  std_logic
        );
    END COMPONENT;
    

   --Inputs
   signal sysClock : std_logic := '0';
   signal switches : std_logic_vector(7 downto 0) := (others => '0');
   signal flow_controlright : std_logic := '0';
   signal flow_controlleft : std_logic := '0';
   signal reset : std_logic := '0';

 	--Outputs
   signal LED : std_logic_vector(7 downto 0);
   signal serialDataOutright : std_logic;
   signal serialDataOutleft : std_logic;

   -- Clock period definitions
   constant sysClock_period : time := 10 ns;
 
BEGIN
 
	-- Instantiate the Unit Under Test (UUT)
   uut: top_level_uart_oopic_test PORT MAP (
          sysClock => sysClock,
          LED => LED,
          switches => switches,
          serialDataOutright => serialDataOutright,
          flow_controlright => flow_controlright,
          serialDataOutleft => serialDataOutleft,
          flow_controlleft => flow_controlleft,
          reset => reset
        );

   -- Clock process definitions
   sysClock_process :process
   begin
		sysClock <= '0';
		wait for sysClock_period/2;
		sysClock <= '1';
		wait for sysClock_period/2;
   end process;
 

   -- Stimulus process
   stim_proc: process
   begin		
      -- hold reset state for 100 ns.
      wait for 100 ns;	

      wait for sysClock_period*10;
		switches <= "10101010";
		flow_controlleft <= '1';
		flow_controlright<= '1';
      -- insert stimulus here 

      wait;
   end process;

END;

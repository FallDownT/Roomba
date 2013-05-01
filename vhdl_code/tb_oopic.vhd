--------------------------------------------------------------------------------
-- Company: 
-- Engineer:
--
-- Create Date:   16:27:55 04/16/2013
-- Design Name:   
-- Module Name:   //VBOXSVR/VBShare/Project 2 Final/uart_oopic_test/tb_oopic.vhd
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
 
ENTITY tb_oopic IS
END tb_oopic;
 
ARCHITECTURE behavior OF tb_oopic IS 
 
    -- Component Declaration for the Unit Under Test (UUT)
 
    COMPONENT top_level_uart_oopic_test
    PORT(
         sysClock : IN  std_logic;
         LED : OUT  std_logic_vector(7 downto 0);
         switches : IN  std_logic_vector(7 downto 0);
         serialDataOutLeft : OUT  std_logic;
         flow_controlLeft : OUT  std_logic;
			serialDataOutRight : OUT  std_logic;
         flow_controlRight : OUT  std_logic;
         reset : IN  std_logic
        );
    END COMPONENT;
    

   --Inputs
   signal sysClock : std_logic := '0';
   signal switches : std_logic_vector(7 downto 0) := (others => '0');
   signal reset : std_logic := '0';

 	--Outputs
   signal LED : std_logic_vector(7 downto 0);
   signal serialDataOutLeft : std_logic;
   signal flow_controlLeft : std_logic;
	signal serialDataOutRight : std_logic;
   signal flow_controlRight : std_logic;

   -- Clock period definitions
   constant sysClock_period : time := 10 ns;
 
BEGIN
 
	-- Instantiate the Unit Under Test (UUT)
   uut: top_level_uart_oopic_test PORT MAP (
          sysClock => sysClock,
          LED => LED,
          switches => switches,
          serialDataOutL => serialDataOutLeft,
          flow_controlL => flow_controlLeft,
			 serialDataOutR => serialDataOutRight,
          flow_controlR => flow_controlRight,
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
		reset <= '0';
      wait for 100 ns;	
		switches <= "01010101";
		wait for 10 ns;
		switches <= "10000101";
      wait for 10 ns;

      -- insert stimulus here 

      wait;
   end process;

END;

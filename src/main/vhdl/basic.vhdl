--/ start vhdl_adder
library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity adder is
  port (
    a, b : in unsigned(7 downto 0);
    sum : out unsigned(7 downto 0)
  );
end entity;



architecture rtl of adder is
begin
  sum <= a + b;
end architecture;
--/ end

library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;



entity ABC is
  port (
    clock : in std_logic;
    reset : in std_logic;
    enable : in std_logic;
    data : in std_logic_vector(7 downto 0);
    q : out std_logic_vector(7 downto 0)
  );
end entity;

architecture rtl of ABC is

--/ start vhdl_register
    signal reg : std_logic_vector(7 downto 0);

begin
    process (clock)
    begin
        if rising_edge(clock) then
            if reset = '1' then
                reg <= (others => '0');
            elsif enable = '1' then
                reg <= data;
            end if;
        end if;
    end process;
--/ end

    q <= reg;

end architecture;

-- the following code was all written by Copilot!
-- but contained errors (missing process in the TB,
-- missing library input,...)

-- adder_tb.vhd
library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity adder_tb is
end entity;

architecture behavioral of adder_tb is

    signal clk_tb, rst_tb : STD_LOGIC := '0';
    signal d_tb, q_tb : STD_LOGIC_VECTOR(7 downto 0) := (others => '0');
    constant clk_period : time := 10 ns;

  component ABC
  port (
    clock : in std_logic;
    reset : in std_logic;
    enable : in std_logic;
    data : in std_logic_vector(7 downto 0);
    q : out std_logic_vector(7 downto 0)
  );
  end component;

--/ start vhdl_use_adder
  signal in1, in2, result : unsigned(7 downto 0);

  component adder
    port (
      a, b : in unsigned(7 downto 0);
      sum : out unsigned(7 downto 0)
    );
  end component;

begin

    m: adder
        port map (
        a => in1,
        b => in2,
        sum => result
        );
--/ end


  dut : ABC
    port map (
      clock => clk_tb,
      reset => rst_tb,
      enable => '1',
      data => d_tb,
      q => q_tb
    );

        process
        begin
            while now < 100 ns loop
                clk_tb <= not clk_tb;
                wait for clk_period / 2;
            end loop;
            wait;
        end process;

process
begin
    in1 <= "00000000";
    in2 <= "00000000";
    wait for 10 ns;
    assert result = "00000000" report "0 + 0 = 0" severity note;

    in1 <= "00000000";
    in2 <= "00000001";
    wait for 10 ns;
    assert result = "00000001" report "0 + 1 = 1" severity note;

    in1 <= "00000001";
    in2 <= "00000000";
    wait for 10 ns;
    assert result = "00000001" report "1 + 0 = 1" severity note;

    in1 <= "00000001";
    in2 <= "00000001";
    wait for 10 ns;
    assert result = "00000010" report "1 + 1 = 2" severity note;

    in1 <= "11111111";
    in2 <= "00000001";
    wait for 10 ns;
    assert result = "00000000" report "255 + 1 = 0" severity note;

    in1 <= "11111111";
    in2 <= "11111111";
    wait for 10 ns;
    assert result = "11111110" report "255 + 255 = 510" severity note;



        wait for 20 ns;  -- Wait for initial signal stability

        -- Test with rst = '1'
        rst_tb <= '1';
        wait for clk_period;
        assert q_tb = "00000000" report "Error: ";
        rst_tb <= '0';

        -- Test with data changes
        d_tb <= "10011001";
        assert q_tb = "00000000" report "Error: ";
        wait for clk_period;
        assert q_tb = "10011001" report "Error: ";
        d_tb <= "01010101";
        wait for clk_period;
        assert q_tb = "10011001" report "Error: ";
        d_tb <= "11001100";
        wait for clk_period;

        -- Add more test scenarios as needed

        wait for 50 ns;
        report "Simulation finished" severity note;
        wait;
    end process;
end architecture;
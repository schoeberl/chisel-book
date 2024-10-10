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


library ieee;
use ieee.std_logic_1164.all;

entity Mux4to1 is
  port (
    sel : in std_logic_vector(1 downto 0);
    input : in std_logic_vector(3 downto 0);
    output : out std_logic
  );
end entity;

architecture rtl of Mux4to1 is
begin
--/ start vhdl_case
  process (sel, input)
  begin
    case sel is
      when "00" => output <= input(0);
      when "01" => output <= input(1);
      when "10" => output <= input(2);
      when "11" => output <= input(3);
      when others => output <= '0';
    end case;
  end process;
--/ end
end architecture;

library ieee;
use ieee.std_logic_1164.all;

entity Mux4to1_tb is
end entity;

architecture behavioral of Mux4to1_tb is
  signal sel_tb : std_logic_vector(1 downto 0) := "00";
  signal in_tb : std_logic_vector(3 downto 0) := "0000";
  signal out_tb : std_logic;

  component Mux4to1
    port (
      sel : in std_logic_vector(1 downto 0);
      input : in std_logic_vector(3 downto 0);
      output : out std_logic
    );
  end component;

begin
  uut: Mux4to1
    port map (
      sel => sel_tb,
      input => in_tb,
      output => out_tb
    );

  process
  begin
    -- Test case 1
    in_tb <= "0001"; sel_tb <= "00";
    wait for 10 ns;
    assert (out_tb = '1') report "Test case 1 failed" severity error;

    -- Test case 2
    in_tb <= "0010"; sel_tb <= "01";
    wait for 10 ns;
    assert (out_tb = '1') report "Test case 2 failed" severity error;

    -- Test case 3
    in_tb <= "0100"; sel_tb <= "10";
    wait for 10 ns;
    assert (out_tb = '1') report "Test case 3 failed" severity error;

    -- Test case 4
    in_tb <= "1000"; sel_tb <= "11";
    wait for 10 ns;
    assert (out_tb = '1') report "Test case 4 failed" severity error;

    -- Test case 5
    in_tb <= "0000"; sel_tb <= "00";
    wait for 10 ns;
    assert (out_tb = '0') report "Test case 5 failed" severity error;

    report "All test cases passed" severity note;
    wait;
  end process;
end architecture;

library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity if_else is
  port (
    c1     : in  std_logic;
    c2     : in  std_logic;
    in1    : in  std_logic_vector(7 downto 0);
    in2    : in  std_logic_vector(7 downto 0);
    in3    : in  std_logic_vector(7 downto 0);
    output : out std_logic_vector(7 downto 0)
  );
end entity;

architecture rtl of if_else is
begin
--/ start vhdl_if_else
  process(c1, c2, in1, in2, in3)
  begin
    if c1 = '1' then
      output <= in1;
    elsif c2 = '1' then
      output <= in2;
    else
      output <= in3;
    end if;
  end process;
--/ end
end architecture;

library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity if_else_tb is
end entity;

architecture behavioral of if_else_tb is
  signal c1_tb, c2_tb : std_logic := '0';
  signal in1_tb, in2_tb, in3_tb, output_tb : std_logic_vector(7 downto 0) := (others => '0');

  component if_else
    port (
      c1     : in  std_logic;
      c2     : in  std_logic;
      in1    : in  std_logic_vector(7 downto 0);
      in2    : in  std_logic_vector(7 downto 0);
      in3    : in  std_logic_vector(7 downto 0);
      output : out std_logic_vector(7 downto 0)
    );
  end component;

begin
  uut: if_else
    port map (
      c1 => c1_tb,
      c2 => c2_tb,
      in1 => in1_tb,
      in2 => in2_tb,
      in3 => in3_tb,
      output => output_tb
    );

  process
  begin
    -- Test case 1: c1 = '1', c2 = '0'
    in1_tb <= "00000001";
    in2_tb <= "00000010";
    in3_tb <= "00000011";
    c1_tb <= '1';
    c2_tb <= '0';
    wait for 10 ns;
    assert (output_tb = "00000001") report "Test case 1 failed" severity error;

    -- Test case 2: c1 = '0', c2 = '1'
    c1_tb <= '0';
    c2_tb <= '1';
    wait for 10 ns;
    assert (output_tb = "00000010") report "Test case 2 failed" severity error;

    -- Test case 3: c1 = '0', c2 = '0'
    c1_tb <= '0';
    c2_tb <= '0';
    wait for 10 ns;
    assert (output_tb = "00000011") report "Test case 3 failed" severity error;

    -- Test case 4: c1 = '1', c2 = '1'
    c1_tb <= '1';
    c2_tb <= '1';
    wait for 10 ns;
    assert (output_tb = "00000001") report "Test case 4 failed" severity error;

    report "All test cases passed" severity note;
    wait;
  end process;
end architecture;
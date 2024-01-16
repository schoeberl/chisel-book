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

    wait;
end process;
end architecture;
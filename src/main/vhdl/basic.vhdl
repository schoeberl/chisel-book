library ieee;
use ieee.std_logic_1164.all;

entity adder is
  port (
    a, b : in std_logic_vector(7 downto 0);
    sum : out std_logic_vector(7 downto 0)
  );
end entity;

architecture behavioral of adder is
begin
  sum <= a + b;
end architecture;

-- the following code was all written by Copilot!

-- adder_tb.vhd
library ieee;
use ieee.std_logic_1164.all;

entity adder_tb is
end entity;

architecture behavioral of adder_tb is
  signal a, b, sum : std_logic_vector(7 downto 0);
begin
    uut : entity work.adder
        port map (
        a => a,
        b => b,
        sum => sum
        );

    a <= "00000000";
    b <= "00000000";
    wait for 10 ns;
    assert sum = "00000000" report "0 + 0 = 0" severity note;

    a <= "00000000";
    b <= "00000001";
    wait for 10 ns;
    assert sum = "00000001" report "0 + 1 = 1" severity note;

    a <= "00000001";
    b <= "00000000";
    wait for 10 ns;
    assert sum = "00000001" report "1 + 0 = 1" severity note;

    a <= "00000001";
    b <= "00000001";
    wait for 10 ns;
    assert sum = "00000010" report "1 + 1 = 2" severity note;

    a <= "11111111";
    b <= "00000001";
    wait for 10 ns;
    assert sum = "00000000" report "255 + 1 = 0" severity note;

    a <= "11111111";
    b <= "11111111";
    wait for 10 ns;
    assert sum = "11111110" report "255 + 255 = 510" severity note;

    a <= "11111111";
    b <= "11111111";
    wait for 10 ns;
    assert sum = "11111111" report "255 + 255 = 510" severity note;
    end architecture;
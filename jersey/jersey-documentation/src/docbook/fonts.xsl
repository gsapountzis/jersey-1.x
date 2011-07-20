<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version="1.0">

    <xsl:import href="urn:docbkx:stylesheet"/>

    <!-- program listings -->
    <xsl:attribute-set name="monospace.verbatim.properties">
        <xsl:attribute name="font-size">7pt</xsl:attribute>
    </xsl:attribute-set>

    <!-- inline code, does not work -->
    <xsl:template match="literal">
        <fo:inline font-size="7pt">
            <xsl:apply-templates />
        </fo:inline>
    </xsl:template>
    <xsl:template match="code">
        <fo:inline font-size="7pt">
            <xsl:apply-templates />
        </fo:inline>
    </xsl:template>

</xsl:stylesheet>

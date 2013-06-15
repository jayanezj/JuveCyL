<?xml version="1.0" encoding="UTF-8" ?>

<!-- New document created with EditiX at Mon Jun 03 20:38:22 CEST 2013 -->

<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes" encoding="utf-8"/>
	<xsl:template match="document">
		<resumen>
			<xsl:for-each select="//element">
				<xsl:sort data-type="text" select="attribute[@name='Provincia']/Provincias"/>
				<albergue>
					<provincia>
						<xsl:value-of select="attribute[@name='Provincia']/Provincias"/>
					</provincia>
					<nombre>
						<xsl:value-of select="attribute[@name='Titulo_es']/string"/>
					</nombre>
					<foto>
						<xsl:value-of select="attribute[@name='Imagen']/link/reference"></xsl:value-of>
					</foto>
					<comoLlegar>
						<xsl:value-of select="attribute[@name='ComoLlegar']/text"/>
					</comoLlegar>
					 <localizacion>
					 	<xsl:value-of select="attribute[@name='Localizacion']/text"/>
					 </localizacion>
					<horarios>
						<abierto>
							<xsl:value-of select="attribute[@name='Abierto']/PeriodoAbierto"/>
						</abierto>
						<horario>
							<xsl:value-of select="attribute[@name='PeriodoAbierto']/string"/>
						</horario>
						<horarioLlegada>
							<xsl:value-of select="attribute[@name='HorarioLlegada']/string"/>
						</horarioLlegada>
					</horarios>
					<capacidad>
						<xsl:value-of select="attribute[@name='Capacidad']/string"/>
					</capacidad>
					
					<contacto>
						<telefonos>
							<xsl:for-each select="attribute[@name='Telefono']/array/string">
								<numero>
									<xsl:value-of select="."/>
								</numero>
							</xsl:for-each>
						</telefonos>
						<fax>
							<xsl:value-of select="attribute[@name='Fax']/array/string"/>
						</fax>
						<email>
							<xsl:value-of select="attribute[@name='Email']/array/string"/>
						</email>
						<web>
							<xsl:value-of select="attribute[@name='Web']/array/string"/>
						</web>
					</contacto>
					<horariosPitanza>
						<Desayuno>
							<xsl:value-of select="concat('desde: ', attribute[@name='DesayunoDesde']/HorasDia, ' hasta ', attribute[@name='DesayunoHasta']/HorasDia)"/>
						</Desayuno>
						<Comida>
							<xsl:value-of select="concat('desde: ', attribute[@name='ComidaDesde']/HorasDia, ' hasta ', attribute[@name='ComidaHasta']/HorasDia)"/>
						</Comida>
						<Cena>
							<xsl:value-of select="concat('desde: ', attribute[@name='CenaDesde']/HorasDia, ' hasta ', attribute[@name='ComidaHasta']/HorasDia)"/>
						</Cena>
					</horariosPitanza>
					<servicios>
						<xsl:for-each select="attribute[@name='Servicios']/array/Servicios">
							<servicio>
								<xsl:value-of select="."/>
							</servicio>
						</xsl:for-each>
					</servicios>
					<equipamiento>
						<xsl:for-each select="attribute[@name='Equipamiento']/array/Equipamiento">
							<equipamiento>
								<xsl:value-of select="."/>
							</equipamiento>
						</xsl:for-each>
					</equipamiento>
					<AreaOcio>
						<xsl:for-each select="attribute[@name='AreaOcio']/array/AreasOcio">
							<area>
								<xsl:value-of select="."/>
							</area>
						</xsl:for-each>
					</AreaOcio>
					<Actividades>
						<xsl:for-each select="attribute[@name='Actividades']/array/ActividadesAlojamiento">
							<actividad>
								<xsl:value-of select="."/>
							</actividad>
						</xsl:for-each>
					</Actividades>
				</albergue>
			</xsl:for-each>
		</resumen>
	</xsl:template>
</xsl:stylesheet>

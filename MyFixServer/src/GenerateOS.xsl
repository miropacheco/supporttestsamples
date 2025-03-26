<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>



<xsl:apply-templates mode="corticon_53"/>

				<!--
								<xsl:apply-templates mode="apama_50" /> <xsl:apply-templates mode="pct_21" /> <xsl:apply-templates mode="apama_50" 
					/> <xsl:apply-templates mode="apama_43" /> <xsl:apply-templates mode="pct_30" 
					/> <xsl:apply-templates mode="corticon_52" /> <xsl:apply-templates mode="corticon_53" 
					/> -->
			</body>
		</html>
	</xsl:template>

	<xsl:template match="tbody" mode="apama_50">

		<table border="0" cellpadding="0" style="width: 100%;">
			<tbody>

				<tr valign="middle">
					<td style="background:#cccccc; padding: 3px;">
						<p>
							<strong> Product </strong>
						</p>
					</td>
					<td style="background:#cccccc; padding: 3px;">
						<p>
							<strong>Operating Systems</strong>
						</p>
					</td>
					<td style="background:#cccccc; padding: 3px;">
						<p>
							version
						</p>
					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">

						<strong>Apama Server</strong>
					</td>
					<td
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />


							<xsl:if test="td[1] !=''">

								<xsl:if test="td[5]!=''">
									<p>


										<xsl:value-of select="td[1]" /> <xsl:value-of select="td[5]" />


									</p>
								</xsl:if>

							</xsl:if>


						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">

							<xsl:for-each select="tr">

								<xsl:if test="td[1] !=''">

									<xsl:if test="td[5]!=''">
										<p>

											<strong> version</strong>

										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>

					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<strong>Apama Dashboard Client</strong>
					</td>
					<td>
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />
							<xsl:if test="td[1] !=''">

								<xsl:if test="td[8]!=''">
									<p>

										<xsl:value-of select="td[1]" />
									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
							<xsl:for-each select="tr">
								<xsl:sort select="./td[1]" order="descending" />
								<xsl:if test="td[1] !=''">

									<xsl:if test="td[8]!=''">
										<p>
											<strong> version</strong>

										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>

					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<strong>Apama Developer Studio</strong>
					</td>
					<td>
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />
							<xsl:if test="td[1] !=''">

								<xsl:if test="td[11]!=''">
									<p>
										<xsl:value-of select="td[1]" />
									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
							<xsl:for-each select="tr">

								<xsl:if test="td[1] !=''">

									<xsl:if test="td[11]!=''">
										<p>
											version
										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>
					</td>
				</tr>

			</tbody>
		</table>
	</xsl:template>
	<xsl:template match="tbody" mode="apama_43">

		<table border="0" cellpadding="0" style="width: 100%;">
			<tbody>

				<tr valign="middle">
					<td style="background:#cccccc; padding: 3px;">
						<p>
							<strong> Product </strong>
						</p>
					</td>
					<td style="background:#cccccc; padding: 3px;">
						<p>
							<strong>Operating Systems</strong>
						</p>
					</td>
					<td style="background:#cccccc; padding: 3px;">
						<p>
							version
						</p>
					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">

						<strong>Apama Server</strong>
					</td>
					<td
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />
							<xsl:if test="td[1] !=''">

								<xsl:if test="td[4]!=''">
									<p>

										<xsl:value-of select="td[1]" /> 	<xsl:value-of select="td[4]" /> 

									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">

							<xsl:for-each select="tr">

								<xsl:if test="td[1] !=''">

									<xsl:if test="td[4]!=''">
										<p>

											version

										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>

					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<strong>Apama Dashboard Client</strong>
					</td>
					<td>
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />
							<xsl:if test="td[1] !=''">

								<xsl:if test="td[7]!=''">
									<p>

										<xsl:value-of select="td[1]" /> <xsl:value-of select="td[1]" /> 	<xsl:value-of select="td[7]" />
									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
							<xsl:for-each select="tr">

								<xsl:if test="td[1] !=''">

									<xsl:if test="td[7]!=''">
										<p>

											version
										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>

					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<strong>Apama Developer Studio</strong>
					</td>
					<td>
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />
							<xsl:if test="td[1] !=''">

								<xsl:if test="td[10]!=''">
									<p>
										<xsl:value-of select="td[1]" /> <xsl:value-of select="td[10]" />
										
									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
							<xsl:for-each select="tr">

								<xsl:if test="td[1] !=''">

									<xsl:if test="td[10]!=''">
										<p>
											version
										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>
					</td>
				</tr>

			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="tbody" mode="corticon_52">

		<table border="0" cellpadding="0" style="width: 100%;">
			<tbody>

				<tr valign="middle">
					<td style="background:#cccccc; padding: 3px;" width="30%">
						<p>
							<strong> Product </strong>
						</p>
					</td>
					<td style="background:#cccccc; padding: 3px;" width="40%">
						<p>
							<strong>Operating Systems</strong>
						</p>
					</td>
					<td style="background:#cccccc; padding: 3px;">
						<p>
							version
						</p>
					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">

						<strong>Corticon server - 52</strong>
					</td>
					<td
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />
							<xsl:if test="td[1] !=''">

								<xsl:if test="td[13]!=''">
									<p>

										<xsl:value-of select="td[1]" />
										<xsl:value-of select="td[13]" />

									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
							<xsl:for-each select="tr">

								<xsl:if test="td[1] !=''">

									<xsl:if test="td[13]!=''">
										<p>

											version

										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>

					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<strong>Corticon Developer studio</strong>
					</td>
					<td>
						<xsl:for-each select="tr">

							<xsl:if test="td[1] !=''">

								<xsl:if test="td[16]!=''">
									<p>

										<xsl:value-of select="td[1]" />
									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
							<xsl:for-each select="tr">
								<xsl:sort select="./td[1]" order="descending" />
								<xsl:if test="td[1] !=''">

									<xsl:if test="td[16]!=''">
										<p>

											version
										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>

					</td>
				</tr>

			</tbody>
		</table>
	</xsl:template>


	<xsl:template match="tbody" mode="corticon_53">

		<table border="0" cellpadding="0" style="width: 100%;">
			<tbody>

				<tr valign="middle">
					<td style="background:#cccccc; padding: 3px;">
						<p>
							<strong> Product </strong>
						</p>
					</td>
					<td style="background:#cccccc; padding: 3px;">
						<p>
							<strong>Operating Systems</strong>
						</p>
					</td>
					<td style="background:#cccccc; padding: 3px;">
						<p>
							version
						</p>
					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">

						<strong>Corticon server </strong>
					</td>
					<td
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />
							<xsl:if test="td[1] !=''">

								<xsl:if test="td[14]!=''">
									<p>

										<xsl:value-of select="td[1]" />
										<xsl:value-of select="td[14]" />

									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
							<xsl:for-each select="tr">
								<xsl:sort select="./td[1]" order="descending" />
								<xsl:if test="td[1] !=''">

									<xsl:if test="td[14]!=''">
										<p>

											<strong> version</strong>

										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>

					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<strong>Corticon Developer studio</strong>
					</td>
					<td>
						<xsl:for-each select="tr">

							<xsl:if test="td[1] !=''">

								<xsl:if test="td[17]!=''">
									<p>

										<xsl:value-of select="td[1]" />
										<xsl:value-of select="td[1]" />
										<xsl:value-of select="td[14]" />
									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
							<xsl:for-each select="tr">

								<xsl:if test="td[1] !=''">

									<xsl:if test="td[17]!=''">
										<p>

											version
										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>

					</td>
				</tr>

			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="tbody" mode="pct_21">

		<table border="0" cellpadding="0" style="width: 100%;">
			<tbody>

				<tr valign="middle">
					<td style="background:#cccccc; padding: 3px;" width="30%">
						<p>
							<strong> Product </strong>
						</p>
					</td>
					<td style="background:#cccccc; padding: 3px;" width="30%">
						<p>
							<strong>Operating Systems</strong>
						</p>
					</td>
					<td style="background:#cccccc; padding: 3px;">
						<p>
							Version
						</p>
					</td>
				</tr>
				<tr width="30%">
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">

						<strong>Control Tower 21</strong>
					</td>
					<td
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />
							<xsl:if test="td[1] !=''">

								<xsl:if test="td[19]!=''">
									<p>

										<xsl:value-of select="td[1]" />

									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
							<xsl:for-each select="tr">

								<xsl:if test="td[1] !=''">

									<xsl:if test="td[19]!=''">
										<p>

											version

										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>

					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<strong>Analyst Studio 6.1.4</strong>
					</td>
					<td
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />
							<xsl:if test="td[1] !=''">

								<xsl:if test="td[22]!=''">
									<p>

										<xsl:value-of select="td[1]" />
									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
							<xsl:for-each select="tr">

								<xsl:if test="td[1] !=''">

									<xsl:if test="td[22]!=''">
										<p>

											version
										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>

					</td>
				</tr>

			</tbody>
		</table>
	</xsl:template>


	<xsl:template match="tbody" mode="pct_30">

		<table border="0" cellpadding="0" style="width: 100%;">
			<tbody>

				<tr valign="middle">
					<td width="30%" style="background:#cccccc; padding: 3px;">
						<p>
							<strong> Product </strong>
						</p>
					</td>
					<td width="40%" style="background:#cccccc; padding: 3px;">
						<p>
							<strong>Operating Systems</strong>
						</p>
					</td>
					<td style="background:#cccccc; padding: 3px;">
						<p>
							version
						</p>
					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">

						<strong>Control Tower 3.0</strong>
					</td>
					<td
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />
							<xsl:if test="td[1] !=''">

								<xsl:if test="td[20]!=''">
									<p>

										<xsl:value-of select="td[1]" />
										<xsl:value-of select="td[20]" />

									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
							<xsl:for-each select="tr">

								<xsl:if test="td[1] !=''">

									<xsl:if test="td[20]!=''">
										<p>

											version

										</p>
									</xsl:if>

								</xsl:if>
							</xsl:for-each>

						</td>

					</td>
				</tr>
				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<strong>Analyst Studio 7.0.5</strong>
					</td>
					<td>
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />
							<xsl:if test="td[1] !=''">

								<xsl:if test="td[23]!=''">
									<p>
										<xsl:value-of select="td[1]" />
										<xsl:value-of select="td[23]" />
									</p>
								</xsl:if>

							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						</td>

					</td>
				</tr>

				<tr>
					<td valign="top"
						style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						<strong>Analyst Server 7.0.5</strong>
					</td>
					<td>
						<xsl:for-each select="tr">
							<xsl:sort select="./td[1]" order="descending" />

							<xsl:if test="td[1] !=''">




								<xsl:if test="td[26]!=''">
									<p>
										<xsl:value-of select="td[1]" />
										<xsl:value-of select="td[26]" />
									</p>
								</xsl:if>
							</xsl:if>
						</xsl:for-each>
						<td valign="top"
							style="background:#ffffff; padding: 3pt; border-bottom:1px #cccccc solid; border-left: 1px #cccccc solid;">
						</td>

					</td>
				</tr>



			</tbody>
		</table>
	</xsl:template>





</xsl:stylesheet>
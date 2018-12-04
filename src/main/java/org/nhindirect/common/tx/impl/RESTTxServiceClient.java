/* 
Copyright (c) 2010, NHIN Direct Project
All rights reserved.

Authors:
   Greg Meyer      gm2552@cerner.com
 
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
in the documentation and/or other materials provided with the distribution.  Neither the name of the The NHIN Direct Project (nhindirect.org). 
nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS 
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.nhindirect.common.tx.impl;

import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeMessage;

import org.nhindirect.common.rest.exceptions.ServiceException;
import org.nhindirect.common.tx.TxService;
import org.nhindirect.common.tx.impl.feign.TxClient;
import org.nhindirect.common.tx.model.Tx;
import org.nhindirect.common.tx.TxDetailParser;
import org.springframework.stereotype.Component;

@Component
public class RESTTxServiceClient implements TxService
{
	protected TxClient txClient;
	
	protected final TxDetailParser parser;
	
    public RESTTxServiceClient(TxClient txClient, TxDetailParser parser) 
    {	
        this.txClient = txClient;
        
        this.parser = parser;
    }
    
	@Override
	public void trackMessage(MimeMessage msg) throws ServiceException
	{
		txClient.addTx(TxParseUtils.convertMimeMessageToTx(msg, parser));
	}

	@Override
	public void trackMessage(InternetHeaders headers) throws ServiceException
	{
		txClient.addTx(TxParseUtils.convertMimeMessageToTx(TxParseUtils.convertHeadersToMessage(headers, parser), parser));
	}

	@Override
	public void trackMessage(Tx tx) throws ServiceException
	{
		txClient.addTx(tx);
	}

	@Override
	public boolean suppressNotification(MimeMessage msg) throws ServiceException 
	{
		return txClient.supressNotification(TxParseUtils.convertMimeMessageToTx(msg, parser));
	}

	@Override
	public boolean suppressNotification(Tx notificationMessage) throws ServiceException 
	{
		return txClient.supressNotification(notificationMessage);
	}
}
